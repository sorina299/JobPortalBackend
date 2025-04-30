package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.Notification;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;

    public NotificationController(NotificationService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/recruiter")
    public ResponseEntity<List<Notification>> getRecruiterNotifications(@RequestHeader("Authorization") String token) {
        int userId = jwtService.extractUserId(token.substring(7));
        return ResponseEntity.ok(notificationService.getNotifications(userId, "RECRUITER"));
    }

    @GetMapping("/jobseeker")
    public ResponseEntity<List<Notification>> getJobSeekerNotifications(@RequestHeader("Authorization") String token) {
        int userId = jwtService.extractUserId(token.substring(7));
        return ResponseEntity.ok(notificationService.getNotifications(userId, "JOB_SEEKER"));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(
            @RequestHeader("Authorization") String token,
            @RequestParam String userType) {
        int userId = jwtService.extractUserId(token.substring(7));
        return ResponseEntity.ok(notificationService.countUnread(userId, userType));
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("Authorization") String token,
            @RequestParam String userType) {
        int userId = jwtService.extractUserId(token.substring(7));
        notificationService.markAllAsRead(userId, userType);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAll(
            @RequestHeader("Authorization") String token,
            @RequestParam String userType) {
        int userId = jwtService.extractUserId(token.substring(7));
        notificationService.clearAll(userId, userType);
        return ResponseEntity.ok().build();
    }
}

