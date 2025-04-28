package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.JobSeekerExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerExperienceRepository extends JpaRepository<JobSeekerExperience, Integer> {
    List<JobSeekerExperience> findByJobSeekerProfile_UserAccountId(int userId);
}

