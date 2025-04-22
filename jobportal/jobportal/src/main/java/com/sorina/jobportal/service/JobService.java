package com.sorina.jobportal.service;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobCompany;
import com.sorina.jobportal.model.JobLocation;
import com.sorina.jobportal.model.JobSeekerApply;
import com.sorina.jobportal.repository.JobCompanyRepository;
import com.sorina.jobportal.repository.JobLocationRepository;
import com.sorina.jobportal.repository.JobRepository;
import com.sorina.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobCompanyService jobCompanyService;

    @Autowired
    private JobLocationService jobLocationService;

    @Autowired
    private JobCompanyRepository jobCompanyRepository;

    @Autowired
    private JobLocationRepository jobLocationRepository;

    @Autowired
    private JobSeekerApplyRepository jobSeekerApplyRepository;

    // Get all job posts
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get job post by ID
    public Optional<Job> getJobById(Integer id) {
        return jobRepository.findById(id);
    }

    // Create or update job post
    public Job saveJob(Job jobPostActivity) {
        return jobRepository.save(jobPostActivity);
    }

    // Delete job post
    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
    }

    @Transactional
    public void deleteJobWithOrphanCheck(Integer jobId) {
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (optionalJob.isEmpty()) return;

        Job job = optionalJob.get();
        JobCompany company = job.getJobCompanyId();
        JobLocation location = job.getJobLocationId();

        // ✅ Delete job applications first to avoid foreign key constraint violation
        jobSeekerApplyRepository.deleteByJob(job);

        // Now delete the job
        jobRepository.delete(job);
        jobRepository.flush();

        // Orphan cleanup
        boolean isCompanyUsed = jobRepository.existsByJobCompanyId(company);
        if (!isCompanyUsed) {
            jobCompanyRepository.delete(company);
        }

        boolean isLocationUsed = jobRepository.existsByJobLocationId(location);
        if (!isLocationUsed) {
            jobLocationRepository.delete(location);
        }
    }


    public List<Job> getJobsByRecruiterId(Integer recruiterId) {
        return jobRepository.findByPostedById_Id(recruiterId);
    }

    public List<Job> searchJobsByTitleAndLocation(String title, String location) {
        return jobRepository.searchJobsByTitleAndLocation(title, location);
    }
}
