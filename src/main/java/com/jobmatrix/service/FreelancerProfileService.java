package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;

import java.util.UUID;

public interface FreelancerProfileService {
    Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO);
    Freelancer getFreelancerProfileById(UUID freelancerId);
    Freelancer updateFreelancerProfile(Freelancer freelancer);
}
