package com.sorina.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @ManyToOne
    @JoinColumn(name = "postedById", referencedColumnName = "id")
    @JsonIgnoreProperties({"password", "tokens", "authorities"})
    private User postedById;

    @ManyToOne
    @JoinColumn(name = "jobLocationId", referencedColumnName = "Id")
    private JobLocation jobLocationId;

    @ManyToOne
    @JoinColumn(name = "jobCompanyId", referencedColumnName = "Id")
    private JobCompany jobCompanyId;

    @Size(max = 10000)
    private String descriptionOfJob;

    private String jobType;

    private String salary;

    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    private String jobTitle;

    public Job() {
    }

    public Job(Integer jobId, User postedById, JobLocation jobLocationId, JobCompany jobCompanyId, String descriptionOfJob, String jobType, String salary, String remote, Date postedDate, String jobTitle) {
        this.jobId = jobId;
        this.postedById = postedById;
        this.jobLocationId = jobLocationId;
        this.jobCompanyId = jobCompanyId;
        this.descriptionOfJob = descriptionOfJob;
        this.jobType = jobType;
        this.salary = salary;
        this.remote = remote;
        this.postedDate = postedDate;
        this.jobTitle = jobTitle;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public User getPostedById() {
        return postedById;
    }

    public void setPostedById(User postedById) {
        this.postedById = postedById;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobId +
                ", postedById=" + postedById +
                ", jobLocationId=" + jobLocationId +
                ", jobCompanyId=" + jobCompanyId +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobType='" + jobType + '\'' +
                ", salary='" + salary + '\'' +
                ", remote='" + remote + '\'' +
                ", postedDate=" + postedDate +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
