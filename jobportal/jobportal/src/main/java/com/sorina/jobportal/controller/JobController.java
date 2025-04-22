package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobCompany;
import com.sorina.jobportal.model.JobLocation;
import com.sorina.jobportal.model.User;
import com.sorina.jobportal.service.JobCompanyService;
import com.sorina.jobportal.service.JobLocationService;
import com.sorina.jobportal.service.JobService;
import com.sorina.jobportal.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private JobLocationService jobLocationService;

    @Autowired
    private JobCompanyService jobCompanyService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable Integer jobId) {
        return jobService.getJobById(jobId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<?> getJobsByRecruiter(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        int recruiterId = jwtService.extractUserId(jwt);

        return ResponseEntity.ok(jobService.getJobsByRecruiterId(recruiterId));
    }


    @PostMapping
    public ResponseEntity<Job> createJob(
            @RequestHeader("Authorization") String token,
            @RequestBody Job job) {

        String jwt = token.substring(7);
        int recruiterId = jwtService.extractUserId(jwt);

        User recruiter = new User();
        recruiter.setId(recruiterId);
        job.setPostedById(recruiter);

        // Save the Job Location if it doesn't exist
        JobLocation savedLocation = jobLocationService.findOrCreateLocation(job.getJobLocationId());

        // Save the Job Company if it doesn't exist
        JobCompany savedCompany = jobCompanyService.findOrCreateCompany(job.getJobCompanyId());

        // Set the saved location and company in the job post
        job.setJobLocationId(savedLocation);
        job.setJobCompanyId(savedCompany);

        job.setPostedDate(new Date());

        // Save the job
        Job savedJob = jobService.saveJob(job);

        // Return the saved job
        return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer jobId,
            @RequestBody Job updatedJob) {

        // Extract recruiter ID from JWT token
        String jwt = token.substring(7);
        int recruiterId = jwtService.extractUserId(jwt);

        // Get the existing job
        Job existingJob = jobService.getJobById(jobId).orElse(null);
        if (existingJob == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Job not found"));
        }

        // Check if the authenticated recruiter is the owner
        if (existingJob.getPostedById().getId() != recruiterId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","You are not authorized to update this job"));
        }

        // Update location and company
        JobLocation updatedLocation = jobLocationService.findOrCreateLocation(updatedJob.getJobLocationId());
        JobCompany updatedCompany = jobCompanyService.findOrCreateCompany(updatedJob.getJobCompanyId());

        // Update job details
        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setDescriptionOfJob(updatedJob.getDescriptionOfJob());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setRemote(updatedJob.getRemote());

        if (updatedJob.getPostedDate() != null) {
            existingJob.setPostedDate(updatedJob.getPostedDate());
        }

        existingJob.setJobLocationId(updatedLocation);
        existingJob.setJobCompanyId(updatedCompany);

        // Save updated job
        Job savedJob = jobService.saveJob(existingJob);

        return ResponseEntity.ok(savedJob);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> deleteJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer jobId) {

        String jwt = token.substring(7);
        int recruiterId = jwtService.extractUserId(jwt);

        Job jobToDelete = jobService.getJobById(jobId).orElse(null);
        if (jobToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Job not found"));
        }

        if (jobToDelete.getPostedById().getId() != recruiterId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "You are not authorized to delete this job"));
        }

        jobService.deleteJobWithOrphanCheck(jobId);

        return ResponseEntity.ok(Map.of("message", "Job and unused related data deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location
    ) {
        List<Job> jobs = jobService.searchJobsByTitleAndLocation(title, location);
        return ResponseEntity.ok(jobs);
    }

}
