package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;

public interface FreelancerProfileService {
    FreelancerProfileCreationResponse initiateProfileCreation(FreelancerDTO freelancerDTO, String contentType);
}