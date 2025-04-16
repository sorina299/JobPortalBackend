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

@RestController
@RequestMapping("/jobseeker-profile")
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final JwtService jwtService;

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
            @RequestPart(value = "file", required = false) MultipartFile imageFile) {

        try {
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            ObjectMapper objectMapper = new ObjectMapper();
            JobSeekerProfileDTO dto = objectMapper.readValue(profileJson, JobSeekerProfileDTO.class);

            JobSeekerProfile updated = jobSeekerProfileService.updateProfileFromDto(userId, dto, imageFile);
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
}

