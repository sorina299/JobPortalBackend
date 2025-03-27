package com.sorina.jobportal.repository;

import com.sorina.jobportal.model.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobLocationRepository extends JpaRepository<JobLocation, Integer> {
    Optional<JobLocation> findByCityAndStateAndCountry(String city, String state, String country);
}
