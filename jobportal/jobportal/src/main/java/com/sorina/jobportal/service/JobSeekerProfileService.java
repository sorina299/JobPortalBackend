package com.sorina.jobportal.service;

import com.sorina.jobportal.exception.JobSeekerProfileNotFoundException;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerProfileService {

    @Autowired
    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    @Autowired
    private final UserRepository userRepository;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    public JobSeekerProfile getProfileByUserId(int userId) {
        return jobSeekerProfileRepository.findById(userId)
                .orElseThrow(() -> new JobSeekerProfileNotFoundException("Job Seeker Profile not found for user ID: " + userId));
    }

    public JobSeekerProfile updateProfile(int userId, JobSeekerProfile updatedProfile) {
        JobSeekerProfile existingProfile = getProfileByUserId(userId);
        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setEmploymentType(updatedProfile.getEmploymentType());
        existingProfile.setResume(updatedProfile.getResume());
        existingProfile.setProfilePhoto(updatedProfile.getProfilePhoto());
        return jobSeekerProfileRepository.save(existingProfile);
    }
}
