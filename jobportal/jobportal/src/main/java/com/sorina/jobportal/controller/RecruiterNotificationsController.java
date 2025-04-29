package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.RecruiterNotifications;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.RecruiterNotificationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter-notifications")
public class RecruiterNotificationsController {

    private final RecruiterNotificationsService notificationService;
    private final JwtService jwtService;

    public RecruiterNotificationsController(RecruiterNotificationsService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    // Get all notifications
    @GetMapping
    public ResponseEntity<List<RecruiterNotifications>> getNotifications(@RequestHeader("Authorization") String token) {
        int recruiterId = jwtService.extractUserId(token.substring(7));
        List<RecruiterNotifications> notifications = notificationService.getNotificationsForRecruiter(recruiterId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // remove "Bearer "
        int recruiterId = jwtService.extractUserId(jwt);
        int count = notificationService.countUnreadNotifications(recruiterId);
        return ResponseEntity.ok(count);
    }

    // Mark a specific notification as read
    @PutMapping("/{notificationId}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    // Mark all notifications as read
    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@RequestHeader("Authorization") String token) {
        int recruiterId = jwtService.extractUserId(token.substring(7));
        notificationService.markAllAsRead(recruiterId);
        return ResponseEntity.ok().build();
    }

    // Delete a specific notification
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }

    // Delete all notifications
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearNotifications(@RequestHeader("Authorization") String token) {
        int recruiterId = jwtService.extractUserId(token.substring(7));
        notificationService.clearAll(recruiterId);
        return ResponseEntity.ok().build();
    }
}
