package com.jobmatrix.serviceimpl;
import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
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
    private FreelancerDTO inputFreelancerDTO;

    @BeforeEach
    void setup() {
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        freelancerEntity = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
    }

    @Test
    void createFreelancerProfile_shouldStoreS3KeyAndReturnFreelancerAndUploadUrl() throws MalformedURLException {
        when(modelMapper.map(freelancerDTO, Freelancer.class)).thenReturn(freelancerEntity);
        when(modelMapper.map(freelancerEntity, FreelancerDTO.class)).thenReturn(freelancerDTO);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancerEntity);

        FreelancerDTO result = freelancerProfileService.createFreelancerProfile(freelancerDTO);

        assertNotNull(result);
        assertEquals(freelancerDTO, result);
        assertEquals(freelancerDTO.getFreelancerId(), result.getFreelancerId());
        assertEquals(freelancerDTO.getProfilePhotoS3Key(), result.getProfilePhotoS3Key());
        verify(modelMapper, times(1)).map(freelancerDTO, Freelancer.class);
        verify(modelMapper, times(1)).map(freelancerEntity, FreelancerDTO.class);
        verify(freelancerRepository, times(1)).save(freelancerEntity);
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

