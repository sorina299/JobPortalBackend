package com.sorina.jobportal.service;

import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.model.User;
import com.sorina.jobportal.repository.RecruiterProfileRepository;
import com.sorina.jobportal.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    // 🔹 Get Recruiter Profile by User ID
    public RecruiterProfile getRecruiterProfileByUserId(int userId) {
        return recruiterProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found for user ID: " + userId));
    }

    // 🔹 Update Recruiter Profile
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

    // Delete Recruiter Profile
    public void deleteRecruiterProfile(int userId) {
        RecruiterProfile profile = getRecruiterProfileByUserId(userId);
        recruiterProfileRepository.delete(profile);
    }
}
