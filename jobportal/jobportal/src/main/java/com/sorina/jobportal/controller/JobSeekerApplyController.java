package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerApply;
import com.sorina.jobportal.service.JobSeekerApplyService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobseeker/apply")
public class JobSeekerApplyController {

    private final JobSeekerApplyService applyService;
    private final JwtService jwtService;

    public JobSeekerApplyController(JobSeekerApplyService applyService, JwtService jwtService) {
        this.applyService = applyService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/{jobId}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> applyForJob(
            @RequestHeader("Authorization") String token,
            @PathVariable int jobId,
            @RequestPart("resume") MultipartFile resumeFile) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);

        applyService.apply(userId, jobId, resumeFile);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Applied successfully with resume"
        ));
    }


    @GetMapping
    public ResponseEntity<List<JobSeekerApply>> getMyApplications(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        int userId = jwtService.extractUserId(jwt);
        return ResponseEntity.ok(applyService.getApplicationsForUser(userId));
    }
}