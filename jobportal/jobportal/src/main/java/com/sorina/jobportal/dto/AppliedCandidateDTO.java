package com.sorina.jobportal.dto;

import java.util.List;

public class AppliedCandidateDTO {
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String profilePhoto;
    private List<SkillDTO> skills;
    private List<JobSeekerExperienceDTO> experiences;
    private String resumeUrl;

    public AppliedCandidateDTO() {
    }

    public AppliedCandidateDTO(String firstName, String lastName, String city, String state, String country, String profilePhoto, List<SkillDTO> skills, List<JobSeekerExperienceDTO> experiences, String resumeUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.profilePhoto = profilePhoto;
        this.skills = skills;
        this.experiences = experiences;
        this.resumeUrl = resumeUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<SkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDTO> skills) {
        this.skills = skills;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public List<JobSeekerExperienceDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<JobSeekerExperienceDTO> experiences) {
        this.experiences = experiences;
    }

    @Override
    public String toString() {
        return "AppliedCandidateDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", skills=" + skills +
                ", experiences=" + experiences +
                ", resumeUrl='" + resumeUrl + '\'' +
                '}';
    }
}
