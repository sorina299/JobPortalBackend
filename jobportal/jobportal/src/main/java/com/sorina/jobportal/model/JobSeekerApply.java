package com.sorina.jobportal.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "jobId"})
})
public class JobSeekerApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private JobSeekerProfile user;

    @ManyToOne
    @JoinColumn(name = "jobId", referencedColumnName = "jobId")
    private Job job;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date applyDate;

    private String resume;

    @Column(name = "cv_viewed")
    private boolean cvViewed = false;

    public JobSeekerApply() {
    }

    public JobSeekerApply(Integer id, JobSeekerProfile user, Job job, Date applyDate, String resume, boolean cvViewed) {
        this.id = id;
        this.user = user;
        this.job = job;
        this.applyDate = applyDate;
        this.resume = resume;
        this.cvViewed = cvViewed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JobSeekerProfile getUser() {
        return user;
    }

    public void setUser(JobSeekerProfile user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isCvViewed() {
        return cvViewed;
    }

    public void setCvViewed(boolean cvViewed) {
        this.cvViewed = cvViewed;
    }

    @Override
    public String toString() {
        return "JobSeekerApply{" +
                "id=" + id +
                ", user=" + user +
                ", job=" + job +
                ", applyDate=" + applyDate +
                ", resume='" + resume + '\'' +
                ", cvViewed=" + cvViewed +
                '}';
    }
}