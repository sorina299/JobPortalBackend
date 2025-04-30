package com.sorina.jobportal.service;

import com.sorina.jobportal.model.Notification;
import com.sorina.jobportal.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void sendNotification(int userId, String userType, String message) {
        Notification notification = new Notification(userId, userType, message);
        repository.save(notification);
    }

    public List<Notification> getNotifications(int userId, String userType) {
        return repository.findByUserIdAndUserTypeOrderByCreatedAtDesc(userId, userType);
    }

    public int countUnread(int userId, String userType) {
        return repository.countByUserIdAndUserTypeAndIsReadFalse(userId, userType);
    }

    public void markAsRead(Long id) {
        Notification n = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        n.setRead(true);
        repository.save(n);
    }

    public void markAllAsRead(int userId, String userType) {
        List<Notification> all = repository.findByUserIdAndUserTypeOrderByCreatedAtDesc(userId, userType);
        all.forEach(n -> n.setRead(true));
        repository.saveAll(all);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void clearAll(int userId, String userType) {
        repository.deleteByUserIdAndUserType(userId, userType);
    }
}
