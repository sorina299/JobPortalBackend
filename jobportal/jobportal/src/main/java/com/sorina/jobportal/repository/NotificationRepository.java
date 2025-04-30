package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndUserTypeOrderByCreatedAtDesc(int userId, String userType);
    int countByUserIdAndUserTypeAndIsReadFalse(int userId, String userType);
    void deleteByUserIdAndUserType(int userId, String userType);
}

