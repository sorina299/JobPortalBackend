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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Integer id) {
        return jobRepository.findById(id);
    }

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

    public List<Job> advancedSearch(String title, String location, List<String> jobTypes, List<String> remoteOptions, String datePosted) {
        return jobRepository.advancedSearch(
                title,
                location,
                jobTypes,
                remoteOptions,
                computeDateThreshold(datePosted)
        );
    }

    private Date computeDateThreshold(String filter) {
        if (filter == null) return null;
        LocalDate today = LocalDate.now();

        switch (filter.toLowerCase()) {
            case "today":
                return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            case "7days":
                return Date.from(today.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
            case "30days":
                return Date.from(today.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
            default:
                return null;
        }
    }

}
