package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
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
    private FreelancerProfileServiceImpl freelancerProfileServiceById;

    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer freelancerEntity;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    void setup() {
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);

    }

    @Test
    void saveFreelancerProfile_shouldSaveAndReturnFreelancerEntity() {

        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);

        Freelancer result = freelancerProfileServiceById.createFreelancerProfile(inputFreelancerDTO);

        assertNotNull(result);
        assertEquals(freelancerEntity.getFreelancerId(), result.getFreelancerId());
        assertEquals(freelancerEntity.getTitle(), result.getTitle());
        assertEquals(freelancerEntity.getBio(), result.getBio());
        assertEquals(freelancerEntity.getHourlyRate(), result.getHourlyRate());
        assertEquals(freelancerEntity.getAddress(), result.getAddress());
        assertEquals(freelancerEntity.getCity(), result.getCity());
        assertEquals(freelancerEntity.getState(), result.getState());
        assertEquals(freelancerEntity.getCountry(), result.getCountry());
        assertEquals(freelancerEntity.getPostalCode(), result.getPostalCode());
        assertEquals(freelancerEntity.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(freelancerEntity.isAbcMember(), result.isAbcMember());
        assertEquals(freelancerEntity.getProfilePhotoURL(), result.getProfilePhotoURL());
        assertEquals(freelancerEntity.getProfileStatus(), result.getProfileStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(modelMapper).map(inputFreelancerDTO, Freelancer.class);
        verify(freelancerRepository, times(1)).save(any(Freelancer.class));
    }

    @Test
    void saveFreelancerProfile_freelancerIdShouldNotBeNull() {
        inputFreelancerDTO.setFreelancerId(null);
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenThrow(new IllegalArgumentException("freelancer_id cannot be null."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> freelancerProfileServiceById.createFreelancerProfile(inputFreelancerDTO),
                "freelancer_id cannot be null."
        );
        assertEquals("freelancer_id cannot be null.", exception.getMessage());
        verify(freelancerRepository, never()).save(any(Freelancer.class));
    }


    @Test
    void getFreelancerProfileById_shouldReturnFreelancerEntity() {
        UUID freelancerId = UUID.randomUUID();
        Freelancer freelancer = new Freelancer();
        FreelancerDTO expectedDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);

        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(modelMapper.map(freelancer, FreelancerDTO.class)).thenReturn(expectedDTO);

        FreelancerDTO result = freelancerProfileServiceById.getFreelancerProfileById(freelancerId);

        assertNotNull(result);
        assertEquals(expectedDTO, result);
        verify(freelancerRepository).findById(freelancerId);
        verify(modelMapper).map(freelancer, FreelancerDTO.class);

    }
    @Test
    void getFreelancerProfileById_shouldThrowFreelancerNotFoundException() {
        UUID freelancerId = UUID.randomUUID();
        when(freelancerRepository.findById(freelancerId)).thenReturn(Optional.empty());
        FreelancerNotFoundException exception = assertThrows(
                FreelancerNotFoundException.class,
                () -> freelancerProfileServiceById.getFreelancerProfileById(freelancerId)
        );
        assertEquals("Freelancer not found with ID: " + freelancerId, exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);

    }
}

