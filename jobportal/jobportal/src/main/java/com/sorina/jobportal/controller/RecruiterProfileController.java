package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.RecruiterProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final JwtService jwtService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, JwtService jwtService) {
        this.recruiterProfileService = recruiterProfileService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public ResponseEntity<RecruiterProfile> getRecruiterProfile(@RequestHeader("Authorization") String token) {
        // Extract userId from token
        String jwt = token.substring(7); // Remove "Bearer " prefix
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(recruiterProfileService.getRecruiterProfileByUserId(userId));
    }

    @PutMapping("/")
    public ResponseEntity<RecruiterProfile> updateRecruiterProfile(@RequestHeader("Authorization") String token,
                                                                   @RequestBody RecruiterProfile updatedProfile) {
        // Extract userId from token
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(recruiterProfileService.updateRecruiterProfile(userId, updatedProfile));
    }
}
