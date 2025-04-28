package com.sorina.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "experience")
public class JobSeekerExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id")
    @JsonIgnore
    private JobSeekerProfile jobSeekerProfile;

    @Column(name = "is_ongoing")
    private boolean isOngoing;

    public JobSeekerExperience() {
    }

    public JobSeekerExperience(Integer id, String jobTitle, String companyName, String startDate, String endDate, JobSeekerProfile jobSeekerProfile, boolean isOngoing) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobSeekerProfile = jobSeekerProfile;
        this.isOngoing = isOngoing;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        this.jobSeekerProfile = jobSeekerProfile;
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    @Override
    public String toString() {
        return "JobSeekerExperience{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", jobSeekerProfile=" + jobSeekerProfile +
                ", isOngoing=" + isOngoing +
                '}';
    }
}