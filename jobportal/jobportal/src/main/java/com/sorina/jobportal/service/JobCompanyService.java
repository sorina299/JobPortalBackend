package com.sorina.jobportal.service;

import com.sorina.jobportal.model.JobCompany;
import com.sorina.jobportal.repository.JobCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobCompanyService {

    @Autowired
    private JobCompanyRepository jobCompanyRepository;

    public JobCompany findOrCreateCompany(JobCompany company) {
        Optional<JobCompany> existingCompany = jobCompanyRepository.findByName(company.getName());
        return existingCompany.orElseGet(() -> jobCompanyRepository.save(company));
    }
}
