package com.sorina.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorina.jobportal.dto.JobSeekerProfileDTO;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.service.JobSeekerProfileService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/jobseeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final JwtService jwtService;
    private final String UPLOAD_DIR = "uploads/";

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, JwtService jwtService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<JobSeekerProfile> getJobSeekerProfile(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(jobSeekerProfileService.getJobSeekerProfileByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<JobSeekerProfile> updateJobSeekerProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("profile") String profileJson,
            @RequestPart(value = "file", required = false) MultipartFile imageFile,
            @RequestPart(value = "resume", required = false) MultipartFile resumeFile) {

        try {
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            ObjectMapper objectMapper = new ObjectMapper();
            JobSeekerProfileDTO dto = objectMapper.readValue(profileJson, JobSeekerProfileDTO.class);

            JobSeekerProfile updated = jobSeekerProfileService.updateProfileFromDto(userId, dto, imageFile, resumeFile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }


    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {

        try {
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            String imageUrl = jobSeekerProfileService.uploadProfileImage(userId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error saving image");
        }
    }

    @PostMapping("/upload-resume")
    public ResponseEntity<Map<String, String>> uploadResume(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        try {
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            String resumeUrl = jobSeekerProfileService.uploadResume(userId, file);
            Map<String, String> response = new HashMap<>();
            response.put("url", resumeUrl); // ✅ Wrap the string inside JSON
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error saving resume: " + e.getMessage()));
        }
    }

}

