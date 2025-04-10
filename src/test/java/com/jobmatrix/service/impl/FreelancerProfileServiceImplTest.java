package com.jobmatrix.service.impl;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.entity.Freelancer;
import com.jobmatrix.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.serviceimpl.FreelancerProfileServiceImpl;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;

    private UUID freelancerId;
    private FreelancerDTO freelancerDTO;
    private Freelancer freelancer;

    @BeforeEach
    void setUp() {
        freelancerId = UUID.randomUUID();
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
        freelancer = new Freelancer();
    }

    @Test
    void createFreelancerProfile_Success() {
        when(modelMapper.map(any(FreelancerDTO.class), eq(Freelancer.class))).thenReturn(freelancer);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);
        when(modelMapper.map(any(Freelancer.class), eq(FreelancerDTO.class))).thenReturn(freelancerDTO);

        FreelancerDTO result = freelancerProfileService.createFreelancerProfile(freelancerDTO);

        assertNotNull(result);
        verify(freelancerRepository).save(any(Freelancer.class));
        verify(modelMapper).map(freelancerDTO, Freelancer.class);
        verify(modelMapper).map(freelancer, FreelancerDTO.class);
    }

    @Test
    void getFreelancerProfileById_Success() {
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(modelMapper.map(freelancer, FreelancerDTO.class)).thenReturn(freelancerDTO);

        FreelancerDTO result = freelancerProfileService.getFreelancerProfileById(freelancerId);

        assertNotNull(result);
        assertEquals(freelancerDTO, result);
        verify(freelancerRepository).findById(freelancerId);
        verify(modelMapper).map(freelancer, FreelancerDTO.class);
    }

    @Test
    void getFreelancerProfileById_UserNotFound() {
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.empty());

        FreelancerNotFoundException exception = assertThrows(
            FreelancerNotFoundException.class,
            () -> freelancerProfileService.getFreelancerProfileById(freelancerId)
        );

        assertEquals("Freelancer not found at given freelancerId: " + freelancerId, exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);
        verify(modelMapper, never()).map(any(), any());
    }
} 