package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobCompany;
import com.sorina.jobportal.model.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    boolean existsByJobCompanyId(JobCompany company);

    boolean existsByJobLocationId(JobLocation location);

}
