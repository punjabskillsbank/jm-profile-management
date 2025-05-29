package com.jobmatrix.serviceimpl;
import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;
import com.jobmatrix.dto.PresignedUrlResponse;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.jobmatrix.repository.FreelancerRepository;
import com.jobmatrix.service.FileService;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.net.MalformedURLException;
import java.net.URL;
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
    private FileService fileService;

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;
    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO freelancerDTO;
    private Freelancer freelancerEntity;
    private PresignedUrlResponse presignedUrlResponse;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    void setup() {
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
        try {
            URL uploadUrl = new URL("https://s3-upload-url");
            presignedUrlResponse = new PresignedUrlResponse();
            presignedUrlResponse.setS3Key("profile-photos/" + FREELANCER_ID + ".jpg");
            presignedUrlResponse.setUploadUrl(uploadUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to construct upload URL", e);
        }
    }


    @Test
    void initiateProfileCreation_shouldStoreS3KeyAndReturnFreelancerAndUploadUrl() throws MalformedURLException {
        when(fileService.generateProfilePhotoUrl(any(), any())).thenReturn(presignedUrlResponse);
        when(modelMapper.map(freelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(modelMapper.map(freelancerEntity, FreelancerDTO.class)).thenReturn(freelancerDTO);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);

        FreelancerProfileCreationResponse result = freelancerProfileService.initiateProfileCreation(freelancerDTO, "image/jpeg");

        assertNotNull(result);
        assertNotNull(result.getFreelancerDTO());
        assertEquals(freelancerDTO, result.getFreelancerDTO());
        assertEquals(new URL("https://s3-upload-url"), result.getPresignedUrl());
        FreelancerDTO returnedDTO =  result.getFreelancerDTO();
        assertEquals(presignedUrlResponse.getS3Key(), returnedDTO.getProfilePhotoS3Key());
        verify(fileService, times(1)).generateProfilePhotoUrl(FREELANCER_ID.toString(), "image/jpeg");
        verify(modelMapper, times(1)).map(freelancerDTO, Freelancer.class);
        verify(modelMapper, times(1)).map(freelancerEntity, FreelancerDTO.class);
        verify(freelancerRepository, times(1)).save(freelancerEntity);
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

