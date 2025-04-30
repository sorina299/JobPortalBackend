package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.JobSeekerNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekerNotificationRepository extends JpaRepository<JobSeekerNotification, Long> {
    List<JobSeekerNotification> findByJobSeekerIdOrderByCreatedAtDesc(int jobSeekerId);
    int countByJobSeekerIdAndIsReadFalse(int jobSeekerId);
    void deleteByJobSeekerId(int jobSeekerId);
}
