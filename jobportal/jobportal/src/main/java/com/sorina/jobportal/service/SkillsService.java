package com.sorina.jobportal.service;

import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.Skills;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {

    private final SkillsRepository skillsRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JwtService jwtService;

    public SkillsService(SkillsRepository skillsRepository, JobSeekerProfileRepository jobSeekerProfileRepository, JwtService jwtService) {
        this.skillsRepository = skillsRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.jwtService = jwtService;
    }

    public List<Skills> getAllSkills(String token) {
        int userId = jwtService.extractUserId(token.substring(7));
        JobSeekerProfile profile = jobSeekerProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getSkills();
    }

    public Skills addSkill(String token, Skills skill) {
        int userId = jwtService.extractUserId(token.substring(7));
        JobSeekerProfile profile = jobSeekerProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        skill.setJobSeekerProfile(profile);
        return skillsRepository.save(skill);
    }

    public void deleteSkill(int skillId) {
        Skills skill = skillsRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skillsRepository.delete(skill);
    }

    public Skills updateSkill(int skillId, Skills updated) {
        Skills skill = skillsRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        skill.setName(updated.getName());
        skill.setExperienceLevel(updated.getExperienceLevel());
        skill.setYearsOfExperience(updated.getYearsOfExperience());

        return skillsRepository.save(skill);
    }
}

