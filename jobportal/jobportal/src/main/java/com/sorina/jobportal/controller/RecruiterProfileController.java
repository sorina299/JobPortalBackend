package com.sorina.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.RecruiterProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final JwtService jwtService;
    private final String UPLOAD_DIR = "uploads/";

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, JwtService jwtService) {
        this.recruiterProfileService = recruiterProfileService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<RecruiterProfile> getRecruiterProfile(@RequestHeader("Authorization") String token) {
        // Extract userId from token
        String jwt = token.substring(7); // Remove "Bearer " prefix
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(recruiterProfileService.getRecruiterProfileByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<RecruiterProfile> updateRecruiterProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("profile") String profileJson, // Change from RecruiterProfile to String
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            // Convert JSON string to RecruiterProfile object
            ObjectMapper objectMapper = new ObjectMapper();
            RecruiterProfile updatedProfile = objectMapper.readValue(profileJson, RecruiterProfile.class);

            return ResponseEntity.ok(recruiterProfileService.updateRecruiterProfile(userId, updatedProfile, file));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {

        try {
            // Extract userId from JWT token
            String jwt = token.substring(7);
            int userId = jwtService.extractUserId(jwt);

            // Upload the image and get the stored path
            String imageUrl = recruiterProfileService.uploadProfileImage(userId, file);

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error saving image");
        }
    }
}
