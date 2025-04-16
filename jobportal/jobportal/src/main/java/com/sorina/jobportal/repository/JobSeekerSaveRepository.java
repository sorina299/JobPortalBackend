package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {

    boolean existsByUserIdAndJob(JobSeekerProfile userId, Job job);

    Optional<JobSeekerSave> findByUserIdAndJob(JobSeekerProfile userId, Job job);

    List<JobSeekerSave> findByUserId_UserAccountId(int userId);
}
