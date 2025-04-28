package com.sorina.jobportal.dto;

public class JobSeekerExperienceDTO {
    private String jobTitle;
    private String companyName;
    private String startDate;
    private String endDate;
    private boolean ongoing;

    public JobSeekerExperienceDTO() {
    }

    public JobSeekerExperienceDTO(String jobTitle, String companyName, String startDate, String endDate, boolean ongoing) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ongoing = ongoing;
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

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    @Override
    public String toString() {
        return "JobSeekerExperienceDTO{" +
                "jobTitle='" + jobTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", ongoing=" + ongoing +
                '}';
    }
}

