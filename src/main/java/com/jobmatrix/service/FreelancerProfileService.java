package com.jobmatrix.service;

import com.jobmatrix.dto.FreelancerDTO;
import java.util.List;
import java.util.UUID;

public interface FreelancerProfileService {
    FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO);
    FreelancerDTO getFreelancerProfile(UUID freelancerId);
    List<FreelancerDTO> getAllFreelancerProfiles();
    FreelancerDTO updateFreelancerProfile(UUID freelancerId, FreelancerDTO freelancerDTO);
    void deleteFreelancerProfile(UUID freelancerId);
}
