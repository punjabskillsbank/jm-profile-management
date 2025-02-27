package com.jobmatrix.jm_profile_management.serviceimpl;

import com.jobmatrix.jm_profile_management.model.FreelancerDTO;
import com.jobmatrix.jm_profile_management.repository.FreelancerRepository;
import com.jobmatrix.jm_profile_management.service.FreelancerService;
import com.jobmatrix.jm_profile_management.model.FreelancerEntity;
import org.springframework.stereotype.Service;


@Service
public class FreelancerServiceImpl implements FreelancerService {

    private final FreelancerRepository freelancerRepository;

    public FreelancerServiceImpl(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }


    @Override
    public FreelancerDTO saveFreelancerProfile(FreelancerDTO freelancerDTO) {
        FreelancerEntity freelancer = convertToEntity(freelancerDTO);
        FreelancerEntity savedFreelancerProfile = freelancerRepository.save(freelancer);
        return convertToDTO(savedFreelancerProfile);
    }


    private FreelancerDTO convertToDTO(FreelancerEntity freelancer) {
        return new FreelancerDTO(freelancer.getSub(),freelancer.getTitle(), freelancer.getBio(),freelancer.getHourlyRate(),freelancer.getAddress(),freelancer.getPhoneNumber(),freelancer.isAbcMember(),freelancer.getProfilePhoto(),freelancer.getEducation(),freelancer.getWorkExperience(),freelancer.getCertificates(), freelancer.getProfileStatus());
    }

    private FreelancerEntity convertToEntity(FreelancerDTO freelancerDTO) {
        FreelancerEntity freelancer = new FreelancerEntity();
        freelancer.setSub(freelancerDTO.sub());
        freelancer.setTitle(freelancerDTO.title());
        freelancer.setBio(freelancerDTO.bio());
        freelancer.setHourlyRate(freelancerDTO.hourlyRate());
        freelancer.setAddress(freelancerDTO.address());
        freelancer.setPhoneNumber(freelancerDTO.phoneNumber());
        freelancer.setAbcMember(freelancerDTO.isAbcMember());
        freelancer.setProfilePhoto(freelancerDTO.profilePhoto());
        freelancer.setEducation(freelancerDTO.education());
        freelancer.setWorkExperience(freelancerDTO.workExperience());
        freelancer.setCertificates(freelancerDTO.certificates());
        freelancer.setProfileStatus(freelancerDTO.profileStatus());


        if (freelancer.getEducation() != null) {
            freelancer.getEducation().forEach(edu -> edu.setFreelancer(freelancer));
        }

        if (freelancer.getWorkExperience() != null) {
            freelancer.getWorkExperience().forEach(we -> we.setFreelancer(freelancer));
        }

        if (freelancer.getCertificates() != null) {
            freelancer.getCertificates().forEach(cert -> cert.setFreelancer(freelancer));
        }

        return freelancer;
    }
}
