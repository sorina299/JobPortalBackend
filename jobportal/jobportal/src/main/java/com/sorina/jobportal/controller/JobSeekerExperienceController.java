package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerExperience;
import com.sorina.jobportal.service.JobSeekerExperienceService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobseeker-experience")
public class JobSeekerExperienceController {

    private final JobSeekerExperienceService experienceService;
    private final JwtService jwtService;

    public JobSeekerExperienceController(JobSeekerExperienceService experienceService, JwtService jwtService) {
        this.experienceService = experienceService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<JobSeekerExperience>> getAllExperiences(@RequestHeader("Authorization") String token) {
        int userId = jwtService.extractUserId(token.substring(7));
        return ResponseEntity.ok(experienceService.getExperiencesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<JobSeekerExperience> addExperience(
            @RequestHeader("Authorization") String token,
            @RequestBody JobSeekerExperience experience) {
        int userId = jwtService.extractUserId(token.substring(7));
        return ResponseEntity.ok(experienceService.addExperience(userId, experience));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSeekerExperience> updateExperience(
            @PathVariable("id") int experienceId,
            @RequestBody JobSeekerExperience experience) {
        JobSeekerExperience updated = experienceService.updateExperience(experienceId, experience);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Integer id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
