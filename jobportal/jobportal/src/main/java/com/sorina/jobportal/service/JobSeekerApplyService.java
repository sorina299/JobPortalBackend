package com.sorina.jobportal.service;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobSeekerApply;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.repository.JobRepository;
import com.sorina.jobportal.repository.JobSeekerApplyRepository;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository applyRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository profileRepository;

    public JobSeekerApplyService(JobSeekerApplyRepository applyRepository,
                                 JobRepository jobRepository,
                                 JobSeekerProfileRepository profileRepository) {
        this.applyRepository = applyRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
    }

    public void apply(int userId, int jobId, MultipartFile resumeFile) {
        JobSeekerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        boolean alreadyApplied = applyRepository.existsByUserAndJob(profile, job);
        if (alreadyApplied) throw new RuntimeException("Already applied to this job");

        // ✅ Validate resume file
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new RuntimeException("Resume is required when applying for a job");
        }

        if (!resumeFile.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        try {
            // Save resume locally
            String uploadDir = "uploads/resumes/";
            String fileName = userId + "_" + System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();
            Path resumePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(resumePath.getParent());
            Files.write(resumePath, resumeFile.getBytes());

            // Create application entry
            JobSeekerApply application = new JobSeekerApply();
            application.setUser(profile);
            application.setJob(job);
            application.setApplyDate(new Date());
            application.setResume("/uploads/resumes/" + fileName); // relative path

            applyRepository.save(application);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    public List<JobSeekerApply> getApplicationsForUser(int userId) {
        return applyRepository.findByUser_UserAccountId(userId);
    }
}

