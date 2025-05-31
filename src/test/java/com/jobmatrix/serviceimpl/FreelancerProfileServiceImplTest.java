package com.jobmatrix.serviceimpl;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.dto.FreelancerServicesDTO;
import com.jobmatrix.entity.FreelancerServices;
import com.jobmatrix.exceptionHandling.NullServicesOfferedException;
import com.jobmatrix.exceptionHandling.ServicesOfferedLimitExceededException;
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

import java.util.ArrayList;
import java.util.List;
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
    @Mock
    private FreelancerServicesRepository freelancerServiceRepository;
    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;
    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer freelancerEntity;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    void setup() {
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
        inputFreelancerDTO.setFreelancerId(FREELANCER_ID); // Ensure freelancerId is set
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
    }

    @Test
    void saveFreelancerProfile_shouldSaveAndReturnFreelancerEntity() {

        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);
        when(modelMapper.map(freelancerEntity, FreelancerDTO.class)).thenReturn(freelancerDTO);
        
        // Mock FreelancerServices mapping
        FreelancerServices mockService = new FreelancerServices();
        mockService.setFreelancerId(FREELANCER_ID);
        mockService.setCategoryId(1L);
        when(modelMapper.map(any(FreelancerServicesDTO.class), eq(FreelancerServices.class))).thenReturn(mockService);
        
        doReturn(mockService).when(freelancerServiceRepository).save(any());

        FreelancerDTO result = freelancerProfileService.saveFreelancerProfile(inputFreelancerDTO);

        assertNotNull(result);
        assertEquals(freelancerEntity.getFreelancerId(), result.getFreelancerId());
        assertEquals(freelancerEntity.getTitle(), result.getTitle());
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
        assertEquals(inputFreelancerDTO.getServices(), result.getServices());

        verify(modelMapper).map(inputFreelancerDTO, Freelancer.class);
        verify(freelancerRepository, times(1)).save(any(Freelancer.class));
    }

    @Test
    void saveFreelancerProfile_shouldThrowNullServiceExceptionWhenServicesAreNull() {
        FreelancerDTO invalidFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        invalidFreelancerDTO.setServices(null);

        assertThrows(NullServicesOfferedException.class, () ->
            freelancerProfileService.saveFreelancerProfile(invalidFreelancerDTO));
    }

    @Test
    void saveFreelancerProfile_shouldThrowServiceLimitExceededExceptionWhenServicesExceedLimit() {
        FreelancerDTO invalidFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        List<Long> tooManyServices = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tooManyServices.add((long) i);
        }
        invalidFreelancerDTO.setServices(tooManyServices);

        assertThrows(ServicesOfferedLimitExceededException.class, () ->
            freelancerProfileService.saveFreelancerProfile(invalidFreelancerDTO));
    }

    @Test
    void saveFreelancerProfile_shouldSaveServicesSuccessfully() {
        // Given
        List<Long> expectedServices = inputFreelancerDTO.getServices(); // Use services from factory
        
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);
        doReturn(new FreelancerServices()).when(freelancerServiceRepository).save(any());

        // When
        FreelancerDTO result = freelancerProfileService.saveFreelancerProfile(inputFreelancerDTO);

        // Then
        assertNotNull(result);
        assertEquals(expectedServices, result.getServices());
        verify(freelancerServiceRepository, times(expectedServices.size())).save(any());
    }

    @Test
    void saveFreelancerProfile_freelancerIdShouldNotBeNull() {
        inputFreelancerDTO.setFreelancerId(null);
        when(modelMapper.map(inputFreelancerDTO, Freelancer.class)).thenThrow(new IllegalArgumentException("freelancer_id cannot be null."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> freelancerProfileService.saveFreelancerProfile(inputFreelancerDTO),
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
        FreelancerDTO result = freelancerProfileService.getFreelancerProfileById(freelancerId);
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
                () -> freelancerProfileService.getFreelancerProfileById(freelancerId)
        );
        assertEquals("Freelancer not found with ID: " + freelancerId, exception.getMessage());
        verify(freelancerRepository).findById(freelancerId);

    }
}

