package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.RecruiterNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruiterNotificationsRepository extends JpaRepository<RecruiterNotifications, Long> {
    List<RecruiterNotifications> findByRecruiterIdOrderByCreatedAtDesc(int recruiterId);
    void deleteByRecruiterId(int recruiterId);
    int countByRecruiterIdAndIsReadFalse(int recruiterId);
}

