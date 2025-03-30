package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.entity.Freelancer;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    @Autowired
    private FreelancerRepository freelancerProfileRepository;

    @Override
    public FreelancerDTO createFreelancerProfile(FreelancerDTO freelancerDTO) {
<<<<<<< HEAD
        Freelancer freelancer = convertToEntity(freelancerDTO);
        Freelancer savedFreelancer = freelancerProfileRepository.save(freelancer);
        return convertToDTO(savedFreelancer);
=======
        Freelancer freelancer = freelancerRepository.save(modelMapper.map(freelancerDTO, Freelancer.class));
        logger.info("Freelancer profile created successfully with id: " + freelancer.getFreelancerId());
        return modelMapper.map(freelancer, FreelancerDTO.class);
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
    }

    @Override
    public FreelancerDTO getFreelancerProfile(UUID freelancerId) {
        return freelancerProfileRepository.findById(freelancerId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Freelancer not found with ID: " + freelancerId));
    }

    @Override
    public List<FreelancerDTO> getAllFreelancerProfiles() {
        return freelancerProfileRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerDTO updateFreelancerProfile(UUID freelancerId, FreelancerDTO freelancerDTO) {
        Freelancer existingFreelancer = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found with ID: " + freelancerId));
        
        updateFreelancerFromDTO(existingFreelancer, freelancerDTO);
        Freelancer updatedFreelancer = freelancerProfileRepository.save(existingFreelancer);
        return convertToDTO(updatedFreelancer);
    }

    @Override
    public void deleteFreelancerProfile(UUID freelancerId) {
        if (!freelancerProfileRepository.existsById(freelancerId)) {
            throw new RuntimeException("Freelancer not found with ID: " + freelancerId);
        }
        freelancerProfileRepository.deleteById(freelancerId);
    }

    private Freelancer convertToEntity(FreelancerDTO dto) {
        Freelancer freelancer = new Freelancer();
        freelancer.setFreelancerId(dto.getFreelancerId());
        freelancer.setTitle(dto.getTitle());
        freelancer.setBio(dto.getBio());
        freelancer.setHourlyRate(dto.getHourlyRate());
        freelancer.setAddress(dto.getAddress());
        freelancer.setCity(dto.getCity());
        freelancer.setState(dto.getState());
        freelancer.setCountry(dto.getCountry());
        freelancer.setPostalCode(dto.getPostalCode());
        freelancer.setPhoneNumber(dto.getPhoneNumber());
        freelancer.setProfilePhotoURL(dto.getProfilePhotoURL());
        freelancer.setProfileStatus(dto.getProfileStatus());
        freelancer.setIsAbcMember(dto.getIsAbcMember());
        return freelancer;
    }

    private FreelancerDTO convertToDTO(Freelancer freelancer) {
        FreelancerDTO dto = new FreelancerDTO();
        dto.setFreelancerId(freelancer.getFreelancerId());
        dto.setTitle(freelancer.getTitle());
        dto.setBio(freelancer.getBio());
        dto.setHourlyRate(freelancer.getHourlyRate());
        dto.setAddress(freelancer.getAddress());
        dto.setCity(freelancer.getCity());
        dto.setState(freelancer.getState());
        dto.setCountry(freelancer.getCountry());
        dto.setPostalCode(freelancer.getPostalCode());
        dto.setPhoneNumber(freelancer.getPhoneNumber());
        dto.setProfilePhotoURL(freelancer.getProfilePhotoURL());
        dto.setProfileStatus(freelancer.getProfileStatus());
        dto.setIsAbcMember(freelancer.getIsAbcMember());
        return dto;
    }

    private void updateFreelancerFromDTO(Freelancer freelancer, FreelancerDTO dto) {
        freelancer.setTitle(dto.getTitle());
        freelancer.setBio(dto.getBio());
        freelancer.setHourlyRate(dto.getHourlyRate());
        freelancer.setAddress(dto.getAddress());
        freelancer.setCity(dto.getCity());
        freelancer.setState(dto.getState());
        freelancer.setCountry(dto.getCountry());
        freelancer.setPostalCode(dto.getPostalCode());
        freelancer.setPhoneNumber(dto.getPhoneNumber());
        freelancer.setProfilePhotoURL(dto.getProfilePhotoURL());
        freelancer.setProfileStatus(dto.getProfileStatus());
        freelancer.setIsAbcMember(dto.getIsAbcMember());
    }
}  
