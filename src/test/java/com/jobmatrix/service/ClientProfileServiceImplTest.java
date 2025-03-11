package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.ClientEntity;
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

    private ClientEntity clientEntity;
    private ClientDTO clientProfileDTO;

    @BeforeEach
    void setup(){

        UUID userId = UUID.randomUUID();

        clientProfileDTO = new ClientDTO();
        clientProfileDTO.setUserId(userId);
        clientProfileDTO.setPhoneNumber("+919876543210");
        clientProfileDTO.setBio("Sample Bio");
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setState("Punjab");
        clientProfileDTO.setPostalCode("141003");

        clientEntity = new ClientEntity();
        clientEntity.setUserId(userId);
        clientEntity.setPhoneNumber("+919876543210");
        clientEntity.setBio("Sample Bio");
        clientEntity.setCompanyName("ABC");
        clientEntity.setState("Punjab");
        clientEntity.setPostalCode("141003");
    }

    @Test
    void saveClientProfile_shouldSaveAndReturnClientEntity(){

        when(modelMapper.map(clientProfileDTO,ClientEntity.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        ClientEntity savedClientEntity = clientProfileService.saveClientProfile(clientProfileDTO);

        assertNotNull(savedClientEntity);
        assertEquals(clientProfileDTO.getUserId(), savedClientEntity.getUserId());
        assertEquals(clientProfileDTO.getPhoneNumber(), savedClientEntity.getPhoneNumber());
        assertEquals(clientProfileDTO.getBio(), savedClientEntity.getBio());
        assertEquals(clientProfileDTO.getCompanyName(), savedClientEntity.getCompanyName());
        assertEquals(clientProfileDTO.getState(), savedClientEntity.getState());
        assertEquals(clientProfileDTO.getPostalCode(), savedClientEntity.getPostalCode());

        verify(modelMapper, times(1)).map(clientProfileDTO, ClientEntity.class);
        verify(clientProfileRepository, times(1)).save(any(ClientEntity.class));
    }

    @Test
    void saveClientProfile_user_idShouldNotBeNull(){

        clientProfileDTO.setUserId(null);

       IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class, ()->
                clientProfileService.saveClientProfile(clientProfileDTO),
                "user_id cannot be null. It must be linked to a user");

       assertEquals("user_id cannot be null. It must be linked to a user", exception.getMessage());

       verify(clientProfileRepository, never()).save(any(ClientEntity.class));

    }

    //test for checking our timestamps updated for created_at and updated_at
    @Test
    void saveClientProfile_shouldUpdateTimestamps(){

        when(modelMapper.map(clientProfileDTO,ClientEntity.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        ClientEntity savedClientEntity = clientProfileService.saveClientProfile(clientProfileDTO);

        assertNotNull(savedClientEntity.getCreated_at());
        assertNotNull(savedClientEntity.getUpdated_at());

        verify(modelMapper, times(1)).map(clientProfileDTO, ClientEntity.class);
        verify(clientProfileRepository, times(1)).save(any(ClientEntity.class));
    }
}