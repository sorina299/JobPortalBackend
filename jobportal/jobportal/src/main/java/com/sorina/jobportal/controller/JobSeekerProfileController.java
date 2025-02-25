package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.service.JobSeekerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobseeker")
public class JobSeekerProfileController {

    @Autowired
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerProfileController(JobSeekerProfileService jobSeekerProfileService) {
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<JobSeekerProfile> getProfile(@PathVariable int userId) {
        return ResponseEntity.ok(jobSeekerProfileService.getProfileByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<JobSeekerProfile> updateProfile(@PathVariable int userId,
                                                          @RequestBody JobSeekerProfile profile) {
        return ResponseEntity.ok(jobSeekerProfileService.updateProfile(userId, profile));
    }
}

