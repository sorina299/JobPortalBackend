package com.sorina.jobportal.service;

import com.sorina.jobportal.model.RecruiterNotifications;
import com.sorina.jobportal.repository.RecruiterNotificationsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecruiterNotificationsService {

    private final RecruiterNotificationsRepository repository;

    public RecruiterNotificationsService(RecruiterNotificationsRepository repository) {
        this.repository = repository;
    }

    public List<RecruiterNotifications> getNotificationsForRecruiter(int recruiterId) {
        return repository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
    }

    public void sendNotification(int recruiterId, String message) {
        RecruiterNotifications notification = new RecruiterNotifications(recruiterId, message);
        repository.save(notification);
    }

    public int countUnreadNotifications(int recruiterId) {
        return repository.countByRecruiterIdAndIsReadFalse(recruiterId);
    }

    public void markAsRead(Long notificationId) {
        RecruiterNotifications notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        repository.save(notification);
    }

    public void markAllAsRead(int recruiterId) {
        List<RecruiterNotifications> notifications = repository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
        for (RecruiterNotifications notification : notifications) {
            notification.setRead(true);
        }
        repository.saveAll(notifications);
    }

    public void deleteNotification(Long notificationId) {
        if (!repository.existsById(notificationId)) {
            throw new RuntimeException("Notification not found");
        }
        repository.deleteById(notificationId);
    }


    @Transactional
    public void clearAll(int recruiterId) {
        repository.deleteByRecruiterId(recruiterId);
    }
}
