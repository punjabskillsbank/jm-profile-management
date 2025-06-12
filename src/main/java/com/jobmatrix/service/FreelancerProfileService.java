package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.dto.ProfileVisibilityDTO;
import com.common.entity.Freelancer;

import java.util.UUID;
public interface FreelancerProfileService {
    FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO);
    FreelancerDTO getFreelancerProfileById(UUID freelancerId);
    void updateProfileVisibility(ProfileVisibilityDTO dto);
}
