package com.sorina.jobportal.service;

import com.sorina.jobportal.model.JobLocation;
import com.sorina.jobportal.repository.JobLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobLocationService {

    @Autowired
    private JobLocationRepository jobLocationRepository;

    public JobLocation findOrCreateLocation(JobLocation location) {
        Optional<JobLocation> existingLocation = jobLocationRepository.findByCityAndStateAndCountry(
                location.getCity(), location.getState(), location.getCountry()
        );
        return existingLocation.orElseGet(() -> jobLocationRepository.save(location));
    }
}