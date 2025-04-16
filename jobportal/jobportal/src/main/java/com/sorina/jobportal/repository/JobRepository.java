package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.Job;
import com.sorina.jobportal.model.JobCompany;
import com.sorina.jobportal.model.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {

    boolean existsByJobCompanyId(JobCompany company);

    boolean existsByJobLocationId(JobLocation location);

    List<Job> findByPostedById_Id(Integer recruiterId);

    // New search method
    @Query("SELECT j FROM Job j WHERE LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.jobLocationId.city) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.jobLocationId.state) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.jobLocationId.country) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchJobsByKeyword(@Param("keyword") String keyword);

}
