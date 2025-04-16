package com.sorina.jobportal.service;

import com.sorina.jobportal.dto.JobSeekerProfileDTO;
import com.sorina.jobportal.exception.JobSeekerProfileNotFoundException;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.SkillsRepository;
import com.sorina.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    private SkillsRepository skillsRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UserRepository userRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.userRepository = userRepository;
    }

    public JobSeekerProfile getJobSeekerProfileByUserId(int userId) {
        return jobSeekerProfileRepository.findById(userId)
                .orElseThrow(() -> new JobSeekerProfileNotFoundException("Job Seeker Profile not found for user ID: " + userId));
    }

    @Transactional
    public JobSeekerProfile updateProfileFromDto(int userId, JobSeekerProfileDTO dto, MultipartFile imageFile) throws IOException {
        JobSeekerProfile profile = getJobSeekerProfileByUserId(userId);

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setCity(dto.getCity());
        profile.setState(dto.getState());
        profile.setCountry(dto.getCountry());

        // Save profile photo if uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageFileName = userId + "_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, imageFileName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageFile.getBytes());
            profile.setProfilePhoto("/uploads/" + imageFileName);
        } else {
            profile.setProfilePhoto(dto.getProfilePhoto()); // fallback
        }

        return jobSeekerProfileRepository.save(profile);
    }

    public String uploadProfileImage(int userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        String imageUrl = "/uploads/" + fileName;

        JobSeekerProfile jobSeekerProfile = getJobSeekerProfileByUserId(userId);
        jobSeekerProfile.setProfilePhoto(imageUrl);
        jobSeekerProfileRepository.save(jobSeekerProfile);

        return imageUrl;
    }
}
