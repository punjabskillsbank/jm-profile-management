package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import com.jobmatrix.exceptionHandling.ClientNotFoundException;
import com.jobmatrix.repository.ClientProfileRepository;
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
class ClientProfileServiceImplTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientProfileServiceImpl clientProfileService;

    private Client testClient;
    private ClientDTO testClientDTO;
    private ClientUpdateRequest testUpdateRequest;
    private UUID testClientId;

    @BeforeEach
    void setUp() {
        testClientId = UUID.randomUUID();
        testClient = Client.builder()
                .clientId(testClientId)
                .companyName("Test Company")
                .phoneNumber("1234567890")
                .bio("Test Bio")
                .industry("Technology")
                .build();

        testClientDTO = new ClientDTO();
        testClientDTO.setClientId(testClientId);
        testClientDTO.setCompanyName("Test Company");
        testClientDTO.setPhoneNumber("1234567890");
        testClientDTO.setBio("Test Bio");
        testClientDTO.setIndustry("Technology");

        testUpdateRequest = new ClientUpdateRequest();
        testUpdateRequest.setCompanyName("Updated Company");
        testUpdateRequest.setPhoneNumber("9876543210");
        testUpdateRequest.setBio("Updated Bio");
        testUpdateRequest.setIndustry("Updated Industry");
    }

    @Test
    void saveClientProfile_Success() {
        // Arrange
        when(modelMapper.map(any(ClientDTO.class), eq(Client.class))).thenReturn(testClient);
        when(clientProfileRepository.save(any(Client.class))).thenReturn(testClient);

        // Act
        Client result = clientProfileService.saveClientProfile(testClientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testClient.getCompanyName(), result.getCompanyName());
        assertEquals(testClient.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testClient.getBio(), result.getBio());
        assertEquals(testClient.getIndustry(), result.getIndustry());
        verify(clientProfileRepository).save(any(Client.class));
        verify(modelMapper).map(any(ClientDTO.class), eq(Client.class));
    }

    @Test
    void getClientProfileById_Success() {
        // Arrange
        when(clientProfileRepository.findById(testClientId)).thenReturn(Optional.of(testClient));

        // Act
        Client result = clientProfileService.getClientProfileById(testClientId);

        // Assert
        assertNotNull(result);
        assertEquals(testClient.getClientId(), result.getClientId());
        assertEquals(testClient.getCompanyName(), result.getCompanyName());
        assertEquals(testClient.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(testClient.getBio(), result.getBio());
        assertEquals(testClient.getIndustry(), result.getIndustry());
        verify(clientProfileRepository).findById(testClientId);
    }

    @Test
    void getClientProfileById_NotFound() {
        // Arrange
        when(clientProfileRepository.findById(testClientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> 
            clientProfileService.getClientProfileById(testClientId)
        );
        verify(clientProfileRepository).findById(testClientId);
    }

    @Test
    void updateClientProfile_Success() {
        // Arrange
        when(clientProfileRepository.findById(testClientId)).thenReturn(Optional.of(testClient));
        when(clientProfileRepository.save(any(Client.class))).thenReturn(testClient);
        doNothing().when(modelMapper).map(any(ClientUpdateRequest.class), any(Client.class));

        // Act
        Client result = clientProfileService.updateClientProfile(testClientId, testUpdateRequest);

        // Assert
        assertNotNull(result);
        verify(clientProfileRepository).findById(testClientId);
        verify(modelMapper).map(testUpdateRequest, testClient);
        verify(clientProfileRepository).save(testClient);
    }

    @Test
    void updateClientProfile_NotFound() {
        // Arrange
        when(clientProfileRepository.findById(testClientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> 
            clientProfileService.updateClientProfile(testClientId, testUpdateRequest)
        );
        verify(clientProfileRepository).findById(testClientId);
        verify(clientProfileRepository, never()).save(any(Client.class));
    }
} 