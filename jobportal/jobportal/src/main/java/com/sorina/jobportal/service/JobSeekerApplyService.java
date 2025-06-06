package com.sorina.jobportal.service;

import com.sorina.jobportal.dto.AppliedCandidateDTO;
import com.sorina.jobportal.dto.JobSeekerExperienceDTO;
import com.sorina.jobportal.dto.SkillDTO;
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
    private final NotificationService notificationService; // ✅ NEW

    public JobSeekerApplyService(
            JobSeekerApplyRepository applyRepository,
            JobRepository jobRepository,
            JobSeekerProfileRepository profileRepository,
            NotificationService notificationService // ✅ Inject it
    ) {
        this.applyRepository = applyRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
        this.notificationService = notificationService; // ✅
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
            application.setResume("/uploads/resumes/" + fileName);

            applyRepository.save(application);

            // ✅ Send notification to the recruiter who posted the job
            int recruiterId = job.getPostedById().getId();
            String fullName = profile.getFirstName() + " " + profile.getLastName();
            String message = fullName + " has applied for your job: " + job.getJobTitle();
            notificationService.sendNotification(recruiterId,"RECRUITER", message);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    public List<JobSeekerApply> getApplicationsForUser(int userId) {
        return applyRepository.findByUser_UserAccountId(userId);
    }

    public List<AppliedCandidateDTO> getCandidatesByJobId(int jobId) {
        List<JobSeekerApply> applications = applyRepository.findByJob_JobId(jobId);

        return applications.stream().map(application -> {
            JobSeekerProfile profile = application.getUser();

            List<SkillDTO> skills = profile.getSkills().stream().map(skill ->
                    new SkillDTO(
                            skill.getName(),
                            skill.getExperienceLevel(),
                            skill.getYearsOfExperience()
                    )
            ).toList();

            List<JobSeekerExperienceDTO> experiences = profile.getExperiences().stream().map(exp ->
                    new JobSeekerExperienceDTO(
                            exp.getJobTitle(),
                            exp.getCompanyName(),
                            exp.getStartDate(),
                            exp.getEndDate(),
                            exp.isOngoing()
                    )
            ).toList();

            return new AppliedCandidateDTO(
                    profile.getFirstName(),
                    profile.getLastName(),
                    profile.getCity(),
                    profile.getState(),
                    profile.getCountry(),
                    profile.getProfilePhoto(),
                    skills,
                    experiences,
                    application.getResume()
            );
        }).toList();
    }

    public int countApplicantsByJobId(int jobId) {
        return applyRepository.findByJob_JobId(jobId).size();
    }

    public JobSeekerApply getByResumePath(String resumePath) {
        return applyRepository.findByResume(resumePath).orElse(null);
    }

    public void save(JobSeekerApply application) {
        applyRepository.save(application);
    }

}
