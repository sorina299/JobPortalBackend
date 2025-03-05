package com.sorina.jobportal.service;

import com.sorina.jobportal.exception.RecruiterProfileNotFoundException;
import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.repository.RecruiterProfileRepository;
import com.sorina.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")  // Read from application.properties
    private String uploadDir;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UserRepository userRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    public RecruiterProfile getRecruiterProfileByUserId(int userId) {
        return recruiterProfileRepository.findById(userId)
                .orElseThrow(() -> new RecruiterProfileNotFoundException("Recruiter Profile not found for user ID: " + userId));
    }

    public RecruiterProfile updateRecruiterProfile(int userId, RecruiterProfile updatedProfile, MultipartFile file) throws IOException {
        RecruiterProfile existingProfile = recruiterProfileRepository.findById(userId)
                .orElseThrow(() -> new RecruiterProfileNotFoundException("Recruiter Profile not found for user ID: " + userId));

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setCompany(updatedProfile.getCompany());

        // ✅ Save the image if a file is uploaded
        if (file != null && !file.isEmpty()) {
            String fileName = userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.write(filePath, file.getBytes()); // Save the file
            existingProfile.setProfilePhoto("/uploads/" + fileName); // Store relative path in DB
        }

        return recruiterProfileRepository.save(existingProfile);
    }

    public String uploadProfileImage(int userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Generate a unique filename
        String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Ensure the upload directory exists
        Files.createDirectories(filePath.getParent());

        // Save the image to the directory
        Files.write(filePath, file.getBytes());

        // Construct the URL path
        String imageUrl = "/uploads/" + fileName;

        // Save image path to database
        RecruiterProfile recruiterProfile = getRecruiterProfileByUserId(userId);
        recruiterProfile.setProfilePhoto(imageUrl);
        recruiterProfileRepository.save(recruiterProfile);

        return imageUrl;
    }
}
