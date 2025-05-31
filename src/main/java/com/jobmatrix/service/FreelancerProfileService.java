package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;

import java.util.UUID;
public interface FreelancerProfileService {
    FreelancerDTO saveFreelancerProfile(FreelancerDTO freelancerDTO);
    FreelancerDTO getFreelancerProfileById(UUID freelancerId);
}
