package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.ClientProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ClientDTO saveClientProfile(ClientDTO dto) {
        Client client = modelMapper.map(dto, Client.class);
        Client savedClient = clientProfileRepository.save(client);
        return modelMapper.map(savedClient, ClientDTO.class);
    }

    // ✅ Implementation of the missing method
    @Override
    public List<ClientDTO> getAllClientProfiles() {
        List<Client> clients = clientProfileRepository.findAll();
        return clients.stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList());
    }

    // ✅ Added the method to fetch by ID
    @Override
    public Optional<ClientDTO> getClientProfileById(UUID id) {
        return clientProfileRepository.findById(id)
                .map(client -> modelMapper.map(client, ClientDTO.class));
    }

    @Override
    public ClientDTO getClientProfile(UUID clientId) {
        Client client = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));
        return modelMapper.map(client, ClientDTO.class);
    }
}
