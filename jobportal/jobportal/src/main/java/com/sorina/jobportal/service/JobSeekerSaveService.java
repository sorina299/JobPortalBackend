package com.sorina.jobportal.service;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.JobSeekerSave;
import com.sorina.jobportal.repository.JobRepository;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.JobSeekerSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    private final JobSeekerSaveRepository saveRepository;
    private final JobSeekerProfileRepository profileRepository;
    private final JobRepository jobRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository saveRepository,
                                JobSeekerProfileRepository profileRepository,
                                JobRepository jobRepository) {
        this.saveRepository = saveRepository;
        this.profileRepository = profileRepository;
        this.jobRepository = jobRepository;
    }

    public void save(int userId, int jobId) {
        JobSeekerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        boolean alreadySaved = saveRepository.existsByUserIdAndJob(profile, job);
        if (alreadySaved) throw new RuntimeException("Job already saved");

        JobSeekerSave save = new JobSeekerSave();
        save.setUserId(profile);
        save.setJob(job);

        saveRepository.save(save);
    }

    public void unsave(int userId, int jobId) {
        JobSeekerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobSeekerSave save = saveRepository.findByUserIdAndJob(profile, job)
                .orElseThrow(() -> new RuntimeException("Saved job not found"));

        saveRepository.delete(save);
    }

    public List<JobSeekerSave> getSavedJobs(int userId) {
        return saveRepository.findByUserId_UserAccountId(userId);
    }
}

