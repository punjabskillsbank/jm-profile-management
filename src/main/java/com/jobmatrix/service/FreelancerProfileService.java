package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;

import java.util.UUID;

public interface FreelancerProfileService {
    FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO);
    FreelancerDTO getFreelancerProfileById(UUID freelancerId);
}