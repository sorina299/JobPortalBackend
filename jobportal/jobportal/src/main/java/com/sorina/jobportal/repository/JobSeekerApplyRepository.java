package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobSeekerApply;
import com.sorina.jobportal.model.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {
    List<JobSeekerApply> findByUser_UserAccountId(int userId);
    boolean existsByUserAndJob(JobSeekerProfile user, Job job);

    void deleteByJob(Job job);

    List<JobSeekerApply> findByJob_JobId(int jobId);
    Optional<JobSeekerApply> findByResume(String resumePath);


}
