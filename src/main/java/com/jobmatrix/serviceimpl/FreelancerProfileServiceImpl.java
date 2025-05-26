package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.dto.FreelancerServicesDTO;
import com.jobmatrix.entity.FreelancerServices;
import com.jobmatrix.exceptionHandling.NullServiceException;
import com.jobmatrix.exceptionHandling.ServiceLimitExceededException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.repository.FreelancerServicesRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor

public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = Logger.getLogger(FreelancerProfileServiceImpl.class);
    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;
    private final FreelancerServicesRepository freelancerServiceRepository;



    @Override
    @Transactional
    public Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO) {

        if (freelancerDTO.getServices() == null || freelancerDTO.getServices().isEmpty()) {
            throw new NullServiceException();
        }

        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));

        if (freelancerDTO.getServices() != null && !freelancerDTO.getServices().isEmpty()) {
            // Check if services exceed allowed limit
            if (freelancerDTO.getServices().size() > 10) {
                throw new ServiceLimitExceededException();
            }

            UUID freelancerId = freelancer.getFreelancerId();

            // Create FreelancerServicesDTO for each category
            for (Long categoryId : freelancerDTO.getServices()) {
                FreelancerServicesDTO serviceDTO = new FreelancerServicesDTO();
                serviceDTO.setFreelancerServiceId(null);
                serviceDTO.setFreelancerId(freelancerId);
                serviceDTO.setCategoryId(categoryId);

                // Convert DTO to entity and save
                FreelancerServices mapping = modelMapper.map(serviceDTO, FreelancerServices.class);
                freelancerServiceRepository.save(mapping);
            }

            logger.info("Freelancer profile created successfully with id: " + freelancerId);
        }

        return freelancer;
    }

    @Override

    public FreelancerDTO getFreelancerProfileById(UUID freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException(freelancerId));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }


}
