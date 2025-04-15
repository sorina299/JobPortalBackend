package com.sorina.jobportal.service;

import com.sorina.jobportal.dto.JobSeekerProfileDTO;
import com.sorina.jobportal.dto.SkillDTO;
import com.sorina.jobportal.exception.JobSeekerProfileNotFoundException;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.Skills;
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
import java.util.ArrayList;
import java.util.List;

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
    public JobSeekerProfile updateProfileFromDto(int userId, JobSeekerProfileDTO dto, MultipartFile file) throws IOException {
        JobSeekerProfile profile = getJobSeekerProfileByUserId(userId);

        // Update primitive fields
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setCity(dto.getCity());
        profile.setState(dto.getState());
        profile.setCountry(dto.getCountry());
        profile.setResume(dto.getResume());
        profile.setProfilePhoto(dto.getProfilePhoto());

        // Remove existing skills safely
        if (profile.getSkills() != null) {
            for (Skills s : new ArrayList<>(profile.getSkills())) {
                s.setJobSeekerProfile(null);  // break the link
            }
            profile.getSkills().clear();  // clear current list
        }

        // Add new skills
        if (dto.getSkills() != null) {
            for (SkillDTO s : dto.getSkills()) {
                Skills skill = new Skills();
                skill.setName(s.getName());
                skill.setExperienceLevel(s.getExperienceLevel());
                skill.setYearsOfExperience(s.getYearsOfExperience());
                skill.setJobSeekerProfile(profile); // important!
                profile.getSkills().add(skill);
            }
        }

        // Handle image upload
        if (file != null && !file.isEmpty()) {
            String fileName = userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            profile.setProfilePhoto("/uploads/" + fileName);
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

    public String uploadResume(int userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Ensure only PDFs or DOCX allowed (optional but good practice)
        String contentType = file.getContentType();
        if (!contentType.equals("application/pdf") && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            throw new IllegalArgumentException("Only PDF or DOCX files are allowed");
        }

        // Save file
        String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path resumeDir = Paths.get(uploadDir, "resumes");
        Files.createDirectories(resumeDir);

        Path filePath = resumeDir.resolve(fileName);
        Files.write(filePath, file.getBytes());

        String resumePath = "/uploads/resumes/" + fileName;

        // Update the profile with resume path
        JobSeekerProfile jobSeekerProfile = getJobSeekerProfileByUserId(userId);
        jobSeekerProfile.setResume(resumePath);
        jobSeekerProfileRepository.save(jobSeekerProfile);

        return resumePath;
    }

}
