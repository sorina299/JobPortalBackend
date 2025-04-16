package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerSave;
import com.sorina.jobportal.service.JobSeekerSaveService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobseeker/save")
public class JobSeekerSaveController {

    private final JobSeekerSaveService saveService;
    private final JwtService jwtService;

    public JobSeekerSaveController(JobSeekerSaveService saveService, JwtService jwtService) {
        this.saveService = saveService;
        this.jwtService = jwtService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> saveJob(@RequestHeader("Authorization") String token,
                                                       @PathVariable int jobId) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);
        saveService.save(userId, jobId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Job saved"
        ));

    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> unsaveJob(@RequestHeader("Authorization") String token,
                                            @PathVariable int jobId) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);
        saveService.unsave(userId, jobId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Job unsaved"
        ));
    }

    @GetMapping
    public ResponseEntity<List<JobSeekerSave>> getSavedJobs(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);
        return ResponseEntity.ok(saveService.getSavedJobs(userId));
    }
}

