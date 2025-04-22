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

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.jobLocationId.city) LIKE LOWER(CONCAT('%', :location, '%')) " +
            "OR LOWER(j.jobLocationId.country) LIKE LOWER(CONCAT('%', :location, '%')) " +
            "OR LOWER(j.jobLocationId.state) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Job> searchJobsByTitleAndLocation(@Param("title") String title, @Param("location") String location);


}
