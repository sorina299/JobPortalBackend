package com.sorina.jobportal.service;

import com.sorina.jobportal.exception.JobSeekerProfileNotFoundException;
import com.sorina.jobportal.exception.RecruiterProfileNotFoundException;
import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.repository.RecruiterProfileRepository;
import com.sorina.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public RecruiterProfile getRecruiterProfileByUserId(int userId) {
        return recruiterProfileRepository.findById(userId)
                .orElseThrow(() -> new RecruiterProfileNotFoundException("Recruiter Profile not found for user ID: " + userId));
    }

    public RecruiterProfile updateRecruiterProfile(int userId, RecruiterProfile updatedProfile) {
        RecruiterProfile existingProfile = getRecruiterProfileByUserId(userId);

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setCompany(updatedProfile.getCompany());
        existingProfile.setProfilePhoto(updatedProfile.getProfilePhoto());

        return recruiterProfileRepository.save(existingProfile);
    }
}
