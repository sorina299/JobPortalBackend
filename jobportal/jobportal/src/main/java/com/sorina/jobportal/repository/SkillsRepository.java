package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Integer> {
    List<Skills> findByJobSeekerProfile_UserAccountId(int userId);
    void deleteAllByJobSeekerProfile_UserAccountId(int userId);
}

