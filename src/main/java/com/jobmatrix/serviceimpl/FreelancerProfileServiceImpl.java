package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;
import com.jobmatrix.dto.PresignedUrlResponse;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FileService;
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
    private final FileService fileService;

    @Transactional
    @Override
    public FreelancerProfileCreationResponse initiateProfileCreation(FreelancerDTO freelancerDTO, String contentType) {
        // Generate presigned URLs for the profile photo
        PresignedUrlResponse presignedUrlResponse = fileService.generateProfilePhotoUrl(freelancerDTO.getFreelancerId().toString(), contentType);

        // Set the S3 key in the FreelancerDTO
        freelancerDTO.setProfilePhotoS3Key(presignedUrlResponse.getS3Key());

        // Save the freelancer entity to the database
        Freelancer freelancer = modelMapper.map(freelancerDTO, Freelancer.class);
        freelancer = freelancerRepository.save(freelancer);
        FreelancerDTO outputFreelancerDTO = modelMapper.map(freelancer, FreelancerDTO.class);

        // Log the successful profile creation
        return new FreelancerProfileCreationResponse(outputFreelancerDTO, presignedUrlResponse.getUploadUrl());
    }

    @Override
    public FreelancerDTO getFreelancerProfileById(UUID freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException(freelancerId));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }


}

}