package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.entity.FreelancerService;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.repository.FreelancerServicesRepository;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
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
    @Mock
    private FreelancerServicesRepository freelancerServiceRepository;

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;

    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer freelancerEntity;

    @BeforeEach
    void setup(){
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);

    }

    @Test
    void saveFreelancerProfile_shouldSaveAndReturnFreelancerEntity() {
        // Mock model mapping
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        
        // Mock repository save
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);

        // Execute the service method
        Freelancer result = freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);

        // Verify the result
        assertNotNull(result, "Result should not be null");
        assertEquals(freelancerEntity.getFreelancerId(), result.getFreelancerId() );
        assertEquals(freelancerEntity.getTitle(), result.getTitle());
        assertEquals(freelancerEntity.getBio(), result.getBio());
        assertEquals(freelancerEntity.getHourlyRate(), result.getHourlyRate());
        assertEquals(freelancerEntity.getAddress(), result.getAddress());
        assertEquals(freelancerEntity.getCity(), result.getCity());
        assertEquals(freelancerEntity.getState(), result.getState());
        assertEquals(freelancerEntity.getCountry(), result.getCountry());
        assertEquals(freelancerEntity.getPostalCode(), result.getPostalCode());
        assertEquals(freelancerEntity.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(freelancerEntity.getIsAbcMember(), result.getIsAbcMember());
        assertEquals(freelancerEntity.getProfilePhotoURL(), result.getProfilePhotoURL());
        assertEquals(freelancerEntity.getProfileStatus(), result.getProfileStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        // Verify interactions
        verify(modelMapper, times(1)).map(inputFreelancerDTO, Freelancer.class);
        verify(freelancerRepository, times(1)).save(any(Freelancer.class));
        verifyNoMoreInteractions(freelancerRepository);
        verifyNoMoreInteractions(modelMapper);
    }

    @Test
    void saveFreelancerProfile_withServices_shouldSaveServicesInSortedOrder() {
        // Setup services with unsorted order
        List<Long> unsortedServices = List.of(3L, 1L, 2L);
        inputFreelancerDTO.setServices(unsortedServices);

        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);
        
        // Mock repository methods
        doNothing().when(freelancerServiceRepository).deleteByFreelancerId(FREELANCER_ID);
        
        Freelancer result = freelancerProfileService.createFreelancerProfile(inputFreelancerDTO);

        assertNotNull(result);
        
        // Verify service deletion and saving
        verify(freelancerServiceRepository).deleteByFreelancerId(FREELANCER_ID);
        verify(freelancerServiceRepository, times(3)).save(any(FreelancerService.class));
        verifyNoMoreInteractions(freelancerServiceRepository);
    }


    @Test
    void saveFreelancerProfile_freelancerIdShouldNotBeNull() {
        inputFreelancerDTO.setFreelancerId(null);
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenThrow(new IllegalArgumentException("freelancer_id cannot be null."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> freelancerProfileService.createFreelancerProfile(inputFreelancerDTO),
                "freelancer_id cannot be null."
        );
        assertEquals("freelancer_id cannot be null.", exception.getMessage());
        verify(freelancerRepository, never()).save(any(Freelancer.class));
    }


}
