package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.dto.FreelancerServicesDTO;
import com.jobmatrix.entity.FreelancerServices;
import com.jobmatrix.exceptionHandling.NullServicesOfferedException;
import com.jobmatrix.exceptionHandling.ServicesOfferedLimitExceededException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.repository.FreelancerServicesRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of FreelancerProfileService interface.
 * Provides methods for creating and retrieving freelancer profiles.
 */
@Service
@RequiredArgsConstructor

public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = Logger.getLogger(FreelancerProfileServiceImpl.class);
    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;
    private final FreelancerServicesRepository freelancerServiceRepository;



    @Override
    @Transactional
    public FreelancerDTO saveFreelancerProfile(FreelancerDTO freelancerDTO) {

        if (freelancerDTO.getServices() == null || freelancerDTO.getServices().isEmpty()) { // Check if services are null or empty
            throw new NullServicesOfferedException();
        } else if (freelancerDTO.getServices().size() > 10) {  // Check if services exceed allowed limit
            throw new ServicesOfferedLimitExceededException();
        }

        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));

        UUID freelancerId = freelancer.getFreelancerId();

        // Create FreelancerServicesDTO for each category
        for (Long categoryId : freelancerDTO.getServices()) {
            FreelancerServicesDTO serviceDTO = new FreelancerServicesDTO(freelancerId, categoryId);

            // Convert DTO to entity and save
            FreelancerServices record = modelMapper.map(serviceDTO, FreelancerServices.class);
            freelancerServiceRepository.save(record);
        }

        logger.info("Freelancer profile created successfully with id: " + freelancerId);
        
        // Convert entity back to DTO before returning
        FreelancerDTO responseDTO = modelMapper.map(freelancer, FreelancerDTO.class);
        if (responseDTO == null) {
            responseDTO = new FreelancerDTO();
        }
        responseDTO.setFreelancerId(freelancer.getFreelancerId()); // Set freelancerId from entity
        responseDTO.setServices(freelancerDTO.getServices()); // Preserve the services from the input DTO
        
        return responseDTO;
    }

    @Override

    public FreelancerDTO getFreelancerProfileById(UUID freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException(freelancerId));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }


}
