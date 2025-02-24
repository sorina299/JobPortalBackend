package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.service.RecruiterProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService) {
        this.recruiterProfileService = recruiterProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RecruiterProfile> getRecruiterProfile(@PathVariable int userId) {
        return ResponseEntity.ok(recruiterProfileService.getRecruiterProfileByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<RecruiterProfile> updateRecruiterProfile(@PathVariable int userId, @RequestBody RecruiterProfile profile) {
        return ResponseEntity.ok(recruiterProfileService.updateRecruiterProfile(userId, profile));
    }
}
