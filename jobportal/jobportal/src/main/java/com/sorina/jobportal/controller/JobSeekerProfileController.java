package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.service.JobSeekerProfileService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobseeker-profile")
public class JobSeekerProfileController {

    @Autowired
    private final JobSeekerProfileService jobSeekerProfileService;

    private final JwtService jwtService;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService, JwtService jwtService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public ResponseEntity<JobSeekerProfile> getJobSeekerProfile(@RequestHeader("Authorization") String token) {
        // Extract userId from token
        String jwt = token.substring(7); // Remove "Bearer " prefix
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(jobSeekerProfileService.getJobSeekerProfileByUserId(userId));
    }

    @PutMapping("/")
    public ResponseEntity<JobSeekerProfile> updateJobSeekerProfile(@RequestHeader("Authorization") String token,
                                                                   @RequestBody JobSeekerProfile updatedProfile) {
        // Extract userId from token
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(jobSeekerProfileService.updateProfile(userId, updatedProfile));
    }
}

