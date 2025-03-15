package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.serviceimpl.ClientProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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

    private Client clientEntity;
    private ClientDTO clientProfileDTO;

    @BeforeEach
    void setup(){

        UUID userId = UUID.randomUUID();

        clientProfileDTO = ClientDTO.builder()
                .clientId(userId)
                .phoneNumber("+919876543210")
                .bio("Sample Bio")
                .companyName("ABC")
                .state("Punjab")
                .postalCode("141003")
                .build();

        clientEntity = Client.builder()
                .clientId(userId)
                .phoneNumber("+919876543210")
                .bio("Sample Bio")
                .companyName("ABC")
                .state("Punjab")
                .postalCode("141003")
                .build();
    }

    @Test
    void saveClientProfile_shouldSaveAndReturnClientEntity(){

        when(modelMapper.map(clientProfileDTO, Client.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(Client.class))).thenReturn(clientEntity);

        Client savedClientEntity = clientProfileService.saveClientProfile(clientProfileDTO);

        assertNotNull(savedClientEntity);
        assertEquals(clientProfileDTO.getClientId(), savedClientEntity.getClientId());
        assertEquals(clientProfileDTO.getPhoneNumber(), savedClientEntity.getPhoneNumber());
        assertEquals(clientProfileDTO.getBio(), savedClientEntity.getBio());
        assertEquals(clientProfileDTO.getCompanyName(), savedClientEntity.getCompanyName());
        assertEquals(clientProfileDTO.getState(), savedClientEntity.getState());
        assertEquals(clientProfileDTO.getPostalCode(), savedClientEntity.getPostalCode());

        verify(modelMapper, times(1)).map(clientProfileDTO, Client.class);
        verify(clientProfileRepository, times(1)).save(any(Client.class));
    }

    @Test
    void saveClientProfile_user_idShouldNotBeNull(){

        clientProfileDTO.setClientId(null);

       IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, ()->
                clientProfileService.saveClientProfile(clientProfileDTO),
                "client_id cannot be null.");

       assertEquals("client_id cannot be null.", exception.getMessage());

       verify(clientProfileRepository, never()).save(any(Client.class));

    }

    //test for checking our timestamps updated for created_at and updated_at
    @Test
    void saveClientProfile_shouldUpdateTimestamps(){

        when(modelMapper.map(clientProfileDTO, Client.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(Client.class))).thenReturn(clientEntity);

        Client savedClientEntity = clientProfileService.saveClientProfile(clientProfileDTO);

        assertNotNull(savedClientEntity.getCreatedAt());
        assertNotNull(savedClientEntity.getUpdatedAt());

        verify(modelMapper, times(1)).map(clientProfileDTO, Client.class);
        verify(clientProfileRepository, times(1)).save(any(Client.class));
    }
}