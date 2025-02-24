package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.RecruiterProfile;
import com.sorina.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, Integer> {
    Optional<RecruiterProfile> findByUser(User user);
}

