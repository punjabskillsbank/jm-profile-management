package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.entity.Freelancer;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private final ModelMapper modelMapper;

    private final FreelancerRepository freelancerRepository;

    @Override
    public FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO) {
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }
}
