package com.sorina.jobportal.service;

import com.sorina.jobportal.model.JobSeekerExperience;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.repository.JobSeekerExperienceRepository;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobSeekerExperienceService {

    private final JobSeekerExperienceRepository experienceRepo;
    private final JobSeekerProfileRepository profileRepo;

    public JobSeekerExperienceService(
            JobSeekerExperienceRepository experienceRepo,
            JobSeekerProfileRepository profileRepo) {
        this.experienceRepo = experienceRepo;
        this.profileRepo = profileRepo;
    }

    public List<JobSeekerExperience> getExperiencesByUserId(int userId) {
        return experienceRepo.findByJobSeekerProfile_UserAccountId(userId);
    }

    public JobSeekerExperience addExperience(int userId, JobSeekerExperience experience) {
        validateDates(experience.getStartDate(), experience.getEndDate());
        JobSeekerProfile profile = profileRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Job seeker profile not found"));
        experience.setJobSeekerProfile(profile);

        return experienceRepo.save(experience);
    }

    public JobSeekerExperience updateExperience(int experienceId, JobSeekerExperience updatedExperience) {
        validateDates(updatedExperience.getStartDate(), updatedExperience.getEndDate());

        JobSeekerExperience existing = experienceRepo.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found with ID: " + experienceId));

        existing.setJobTitle(updatedExperience.getJobTitle());
        existing.setCompanyName(updatedExperience.getCompanyName());
        existing.setStartDate(updatedExperience.getStartDate());
        existing.setEndDate(updatedExperience.getEndDate());
        existing.setOngoing(updatedExperience.isOngoing());

        return experienceRepo.save(existing);
    }


    public void deleteExperience(int experienceId) {
        experienceRepo.deleteById(experienceId);
    }

    private void validateDates(String startDate, String endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (end.isBefore(start)) {
                throw new IllegalArgumentException("End date must be after start date");
            }
        }
    }

}


