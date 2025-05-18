package com.jobmatrix.service;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import org.springframework.stereotype.Service;

import java.util.UUID;
public interface FreelancerProfileService {
    Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO);
    FreelancerDTO getFreelancerProfileById(UUID freelancerId);
}
