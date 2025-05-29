package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;

import java.util.UUID;
public interface FreelancerProfileService {
    FreelancerProfileCreationResponse initiateProfileCreation(FreelancerDTO freelancerDTO, String contentType);
    FreelancerDTO getFreelancerProfileById(UUID freelancerId);
}