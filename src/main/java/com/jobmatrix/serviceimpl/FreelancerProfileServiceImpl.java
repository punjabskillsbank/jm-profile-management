package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.dto.PresignedUrlResponse;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FileService;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = Logger.getLogger(FreelancerProfileServiceImpl.class);
    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;
    private final FileService fileService;

    @Transactional
    @Override
    public Object[] initiateProfileCreation(FreelancerDTO freelancerDTO, String contentType) {
        // Generate presigned URLs for the profile photo
        PresignedUrlResponse presignedUrlResponse = fileService.generateProfilePhotoUrls(freelancerDTO.getFreelancerId().toString(), contentType);

        // Set the download URL in the client DTO
        freelancerDTO.setProfilePhotoS3Key(presignedUrlResponse.getS3Key());

        // Save the client profile
        Freelancer freelancer = modelMapper.map(freelancerDTO, Freelancer.class);
        freelancer = freelancerRepository.save(freelancer);

        // Return client and both URLs
        return new Object[]{freelancer, presignedUrlResponse.getUploadUrl()};
    }



    @Override
    @Transactional
    public Freelancer createFreelancerProfile(FreelancerDTO freelancerDTO) {
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        logger.info("Freelancer profile created successfully with id: " + freelancer.getFreelancerId());
        return freelancer;
    }
}