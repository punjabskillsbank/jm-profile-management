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

    private final UUID CLIENT_ID = UUID.randomUUID();
    private Client clientEntity;
    private ClientDTO clientDTO;
    private IllegalArgumentException exception;

    @BeforeEach
    void setup(){
        clientDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);
        clientEntity = ClientTestDataFactory.createClientEntity(CLIENT_ID);
    }

    @Test
    void saveClientProfile_shouldSaveAndReturnClientEntity(){

        when(modelMapper.map(clientDTO, Client.class)).thenReturn(clientEntity);
        when(clientProfileRepository.save(any(Client.class))).thenReturn(clientEntity);

        Client savedClientEntity = clientProfileService.saveClientProfile(clientDTO);

        assertNotNull(savedClientEntity);
        assertEquals(clientDTO.getClientId(), savedClientEntity.getClientId());
        assertEquals(clientDTO.getPhoneNumber(), savedClientEntity.getPhoneNumber());
        assertEquals(clientDTO.getBio(), savedClientEntity.getBio());
        assertEquals(clientDTO.getCompanyName(), savedClientEntity.getCompanyName());
        assertEquals(clientDTO.getState(), savedClientEntity.getState());
        assertEquals(clientDTO.getPostalCode(), savedClientEntity.getPostalCode());

        assertNotNull(savedClientEntity.getCreatedAt());
        assertNotNull(savedClientEntity.getUpdatedAt());

        verify(modelMapper, times(1)).map(clientDTO, Client.class);
        verify(clientProfileRepository, times(1)).save(any(Client.class));
    }

    @Test
    void saveClientProfile_client_idShouldNotBeNull(){
        clientDTO.setClientId(null);
        when(modelMapper.map(clientDTO, Client.class)).thenThrow(new IllegalArgumentException("client_id cannot be null."));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> clientProfileService.saveClientProfile(clientDTO),
                "client_id cannot be null."
        );
        assertEquals("client_id cannot be null.", exception.getMessage());
        verify(clientProfileRepository, never()).save(any(Client.class));
    }

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

    @Test
    void deleteClientProfileById_shouldDeleteClientEntity() {
        when(clientProfileRepository.findById(CLIENT_ID)).thenReturn(Optional.of(clientEntity));

        clientProfileService.deleteClientProfileById(CLIENT_ID);

        verify(clientProfileRepository, times(1)).delete(clientEntity);
    }

    @Test
    void deleteClientProfileById_shouldThrowClientNotFoundException() {
        when(clientProfileRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        ClientNotFoundException exception = assertThrows(
                ClientNotFoundException.class,
                () -> clientProfileService.deleteClientProfileById(CLIENT_ID)
        );

        verify(clientProfileRepository, never()).deleteById(CLIENT_ID);
    }

}