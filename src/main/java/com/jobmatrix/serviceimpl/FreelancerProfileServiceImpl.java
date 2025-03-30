package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.entity.Freelancer;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = Logger.getLogger(FreelancerProfileServiceImpl.class);
    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;

    @Override
    @Transactional
    public FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO) {
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        logger.info("Freelancer profile created successfully with id: " + freelancer.getFreelancerId());
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }

    @Override
    public FreelancerDTO getFreelancerProfile(UUID freelancerId) {
        logger.info("Fetching freelancer profile with id: " + freelancerId);
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new EntityNotFoundException("Freelancer not found with ID: " + freelancerId));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }

    @Override
    public List<FreelancerDTO> getAllFreelancerProfiles() {
        logger.info("Fetching all freelancer profiles");
        List<Freelancer> freelancers = freelancerRepository.findAll();
        return freelancers.stream()
                .map(freelancer -> modelMapper.map(freelancer, FreelancerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerDTO updateFreelancerProfile(UUID freelancerId, FreelancerDTO freelancerDTO) {
        logger.info("Updating freelancer profile with id: " + freelancerId);
        Freelancer existingFreelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new EntityNotFoundException("Freelancer not found with ID: " + freelancerId));
        
        // Update the existingFreelancer with freelancerDTO properties
        modelMapper.map(freelancerDTO, existingFreelancer);
        // Ensure the ID remains the same
        existingFreelancer.setFreelancerId(freelancerId);
        
        Freelancer updatedFreelancer = freelancerRepository.save(existingFreelancer);
        return modelMapper.map(updatedFreelancer, FreelancerDTO.class);
    }

    @Override
    public void deleteFreelancerProfile(UUID freelancerId) {
        logger.info("Deleting freelancer profile with id: " + freelancerId);
        if (!freelancerRepository.existsById(freelancerId)) {
            throw new EntityNotFoundException("Freelancer not found with ID: " + freelancerId);
        }
        freelancerRepository.deleteById(freelancerId);
    }
}
