package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.entity.FreelancerService;
import com.jobmatrix.entity.FreelancerServiceKey;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.repository.FreelancerServicesRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
        // Validate input
        if (freelancerDTO == null) {
            throw new IllegalArgumentException("FreelancerDTO cannot be null");
        }
        
        // Save freelancer first to get the generated ID
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        UUID freelancerId = freelancer.getFreelancerId();
        
        // Map services (category IDs) if provided
        if (freelancerDTO.getServices() != null && !freelancerDTO.getServices().isEmpty()) {
            // Delete existing services for this freelancer first
            freelancerServiceRepository.deleteByFreelancerId(freelancerId);
            
            // Sort category IDs in ascending order
            List<Long> sortedCategories = freelancerDTO.getServices()
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
            
            for (Long categoryId : sortedCategories) {
                FreelancerService mapping = new FreelancerService();
                mapping.setId(new FreelancerServiceKey(freelancerId, categoryId));
                freelancerServiceRepository.save(mapping);
            }
        }

        logger.info("Freelancer profile created successfully with id: " + freelancerId);
        return freelancer;
    }
}
