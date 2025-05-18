package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.dto.ClientDTO;

public interface FreelancerProfileService {
    Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO);
    Object[] initiateProfileCreation(FreelancerDTO freelancerDTO, String contentType);
}