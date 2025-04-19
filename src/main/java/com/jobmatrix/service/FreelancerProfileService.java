package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;

public interface FreelancerProfileService {
    Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO);
}
