package com.jobmatrix.serviceimpl;

import com.common.dto.CategoryDTO;
import com.common.dto.FreelancerDTO;
import com.common.entity.Category;
import com.common.entity.Freelancer;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.exceptionHandling.CategoryNotFound;
import com.jobmatrix.exceptionHandling.NullCategoriesOfferedException;
import com.jobmatrix.exceptionHandling.CategoriesOfferedLimitExceededException;
import com.jobmatrix.repository.CategoryRepository;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FreelancerProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private static final Logger logger = Logger.getLogger(FreelancerProfileServiceImpl.class);
    private final ModelMapper modelMapper;
    private final FreelancerRepository freelancerRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO) {
        if (freelancerDTO.getCategoriesDTO() == null || freelancerDTO.getCategoriesDTO().isEmpty()) { // Check if services are null or empty
            throw new NullCategoriesOfferedException();
        } else if (freelancerDTO.getCategoriesDTO().size() > 10) {  // Check if services exceed allowed limit
            throw new CategoriesOfferedLimitExceededException();
        }

        Freelancer freelancer = modelMapper.map(freelancerDTO, Freelancer.class);

        Set<Category> categories = freelancerDTO.getCategoriesDTO().stream()
                .map(categoryDTO -> categoryRepository.findById(categoryDTO.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFound(categoryDTO.getCategoryId())))
                .collect(Collectors.toSet());

        freelancer.setCategories(categories);

        Freelancer savedFreelancer = freelancerRepository.save(freelancer);

        logger.info("Freelancer profile created with ID: " + savedFreelancer.getFreelancerId());

        // Manually mapping Category entities to CategoryDTOs for response
        Set<CategoryDTO> categoryDTOSet = savedFreelancer.getCategories().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toSet());

        FreelancerDTO responseDTO = modelMapper.map(savedFreelancer, FreelancerDTO.class);
        responseDTO.setCategoriesDTO(categoryDTOSet);

        // Map saved entity back to DTO
        return responseDTO;
    }

    @Override

    public FreelancerDTO getFreelancerProfileById(UUID freelancerId) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new FreelancerNotFoundException(freelancerId));
        return modelMapper.map(freelancer, FreelancerDTO.class);
    }


}
