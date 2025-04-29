package com.sorina.jobportal.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "recruiter_notifications")
public class RecruiterNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int recruiterId;

    private String message;

    private boolean isRead = false;

    private Date createdAt = new Date();

    public RecruiterNotifications() {}

    public RecruiterNotifications(int recruiterId, String message) {
        this.recruiterId = recruiterId;
        this.message = message;
        this.isRead = false;
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RecruiterNotifications{" +
                "id=" + id +
                ", recruiterId=" + recruiterId +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
