package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO) {
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        freelancer = freelancerRepository.save(freelancer);
        logger.info("Freelancer profile created successfully with id: " + freelancer.getFreelancerId());
        return freelancer;
    }

    @Override
    public Freelancer getFreelancerProfileById(UUID freelancerId) {
        logger.info("Retrieving freelancer profile with id: " + freelancerId);
        return freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException(freelancerId));
    }

    @Override
    @Transactional
    public Freelancer updateFreelancerProfile(Freelancer freelancer) {
        logger.info("Updating freelancer profile with id: " + freelancer.getFreelancerId());
        return freelancerRepository.save(freelancer);
    }
}