package com.sorina.jobportal.controller;

import com.sorina.jobportal.model.JobSeekerProfile;
import com.sorina.jobportal.model.Skills;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.SkillsRepository;
import com.sorina.jobportal.service.JwtService;
import com.sorina.jobportal.service.SkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillsController {

    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @GetMapping
    public ResponseEntity<List<Skills>> getAllSkills(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(skillsService.getAllSkills(token));
    }

    @PostMapping
    public ResponseEntity<Skills> addSkill(@RequestHeader("Authorization") String token,
                                           @RequestBody Skills skill) {
        return ResponseEntity.ok(skillsService.addSkill(token, skill));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable int skillId) {
        skillsService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<Skills> updateSkill(@PathVariable int skillId,
                                              @RequestBody Skills skill) {
        return ResponseEntity.ok(skillsService.updateSkill(skillId, skill));
    }
}

