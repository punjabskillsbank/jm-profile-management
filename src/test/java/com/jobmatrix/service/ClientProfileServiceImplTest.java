package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;
import com.jobmatrix.exceptionHandling.ClientNotFoundException;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.serviceimpl.ClientProfileServiceImpl;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
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

    private final UUID CLIENT_ID = UUID.randomUUID();
    private Client clientEntity;
    private ClientDTO clientDTO;

    @BeforeEach
    void setup(){
        clientDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);
        clientEntity = ClientTestDataFactory.createClientEntity(CLIENT_ID);
    }

    @Test
    void saveClientProfile_shouldSaveAndReturnClientDTO() {
        // Mock the mapping and repository behavior
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(Client.class))).thenReturn(clientEntity);
        when(modelMapper.map(clientEntity, ClientDTO.class)).thenReturn(clientDTO);

        // Use ClientDTO in the assertion
        ClientDTO savedClientDTO = clientProfileService.saveClientProfile(clientDTO);

        assertNotNull(savedClientDTO);
        assertEquals(clientDTO.getClientId(), savedClientDTO.getClientId());
        assertEquals(clientDTO.getPhoneNumber(), savedClientDTO.getPhoneNumber());
        assertEquals(clientDTO.getBio(), savedClientDTO.getBio());
        assertEquals(clientDTO.getCompanyName(), savedClientDTO.getCompanyName());
        assertEquals(clientDTO.getState(), savedClientDTO.getState());
        assertEquals(clientDTO.getPostalCode(), savedClientDTO.getPostalCode());

        assertNotNull(savedClientDTO.getCreatedAt());
        assertNotNull(savedClientDTO.getUpdatedAt());

        verify(modelMapper, times(1)).map(clientDTO, Client.class);
        verify(clientProfileRepository, times(1)).save(any(Client.class));
        verify(modelMapper, times(1)).map(clientEntity, ClientDTO.class);
    }

    @Test
<<<<<<< HEAD
    void saveClientProfile_user_idShouldNotBeNull() {
=======
    void saveClientProfile_client_idShouldNotBeNull(){
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
        clientDTO.setClientId(null);
        when(modelMapper.map(clientDTO, Client.class))
                .thenThrow(new IllegalArgumentException("client_id cannot be null."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clientProfileService.saveClientProfile(clientDTO),
                "client_id cannot be null."
        );

        assertEquals("client_id cannot be null.", exception.getMessage());
        verify(clientProfileRepository, never()).save(any(Client.class));
    }
<<<<<<< HEAD
}
=======

    @Test
    void getClientProfileById_shouldReturnClientEntity(){
        when(clientProfileRepository.findById(CLIENT_ID)).thenReturn(Optional.of(clientEntity));

       Client client = clientProfileService.getClientProfileById(CLIENT_ID);
        assertNotNull(client);
        assertEquals(clientEntity.getClientId(), client.getClientId());
        assertEquals(clientEntity.getPhoneNumber(), client.getPhoneNumber());
        assertEquals(clientEntity.getBio(), client.getBio());
        assertEquals(clientEntity.getCompanyName(), client.getCompanyName());
        assertEquals(clientEntity.getState(), client.getState());
        assertEquals(clientEntity.getPostalCode(), client.getPostalCode());

        verify(clientProfileRepository, times(1)).findById(CLIENT_ID);
    }

    @Test
    void getClientProfileById_shouldThrowClientNotFoundException(){
        when(clientProfileRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(
                ClientNotFoundException.class,
                () -> clientProfileService.getClientProfileById(CLIENT_ID),
                "Client not found at given clientId"
        );
        assertEquals("Client not found at given clientId: " + CLIENT_ID, exception.getMessage());
        verify(clientProfileRepository, times(1)).findById(CLIENT_ID);
    }

}
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
