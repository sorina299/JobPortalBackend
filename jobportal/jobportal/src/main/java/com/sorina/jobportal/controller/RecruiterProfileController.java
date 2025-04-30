package com.sorina.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sorina.jobportal.dto.AppliedCandidateDTO;
import com.sorina.jobportal.model.JobSeekerApply;
import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.repository.JobSeekerApplyRepository;
import com.sorina.jobportal.service.JobSeekerApplyService;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.NotificationService;
import com.sorina.jobportal.service.RecruiterProfileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    private final JobSeekerApplyRepository jobSeekerApplyRepository;
    private final String UPLOAD_DIR = "uploads/";

    public RecruiterProfileController(RecruiterProfileService recruiterProfileService, JobSeekerApplyService jobSeekerApplyService, JwtService jwtService, NotificationService notificationService, JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jwtService = jwtService;
        this.notificationService = notificationService;
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
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

    @GetMapping("/applicants/{jobId}")
    public ResponseEntity<List<AppliedCandidateDTO>> getApplicantsForJob(@PathVariable int jobId) {
        return ResponseEntity.ok(jobSeekerApplyService.getCandidatesByJobId(jobId));
    }

    @GetMapping("/download-resume")
    public ResponseEntity<Resource> downloadResume(@RequestParam String path) {
        try {
            Path filePath = Paths.get("uploads/resumes").resolve(path).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // ✅ Find application by resume path
            JobSeekerApply application = jobSeekerApplyService.getByResumePath("/uploads/resumes/" + path);
            if (application != null && !application.isCvViewed()) {
                application.setCvViewed(true);
                jobSeekerApplyService.save(application); // persist update

                // ✅ Send notification to the job seeker
                int jobSeekerId = application.getUser().getUserAccountId();
                String jobTitle = application.getJob().getJobTitle();
                String message = "Your application for the role " + jobTitle + " was viewed.";
                notificationService.sendNotification(jobSeekerId, "JOB_SEEKER", message);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/count-applicants/{jobId}")
    public ResponseEntity<Integer> countApplicants(@PathVariable int jobId) {
        return ResponseEntity.ok(jobSeekerApplyService.countApplicantsByJobId(jobId));
    }
}
