package com.sorina.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    @Id
    @Column(name = "user_account_id")
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    @JsonIgnore
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "profile_photo", nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skills> skills = new ArrayList<>();

    @OneToMany(mappedBy = "jobSeekerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSeekerExperience> experiences = new ArrayList<>();

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(int userAccountId, User user, String firstName, String lastName, String city, String state, String country, String profilePhoto, List<Skills> skills, List<JobSeekerExperience> experiences) {
        this.userAccountId = userAccountId;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.profilePhoto = profilePhoto;
        this.skills = skills;
        this.experiences = experiences;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public List<JobSeekerExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<JobSeekerExperience> experiences) {
        this.experiences = experiences;
    }

    @Override
    public String toString() {
        return "JobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", skills=" + skills +
                ", experiences=" + experiences +
                '}';
    }
}