package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.JobCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobCompanyRepository extends JpaRepository<JobCompany, Integer> {
    Optional<JobCompany> findByName(String name);
}
