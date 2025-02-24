package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {
    Optional<JobSeekerProfile> findByUser(User user);
}

