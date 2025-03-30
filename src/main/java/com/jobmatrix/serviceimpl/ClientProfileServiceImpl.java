package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;
import com.jobmatrix.exceptionHandling.ClientNotFoundException;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.ClientProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
=======
import java.util.UUID;
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b

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

<<<<<<< HEAD
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
=======
    @Override
    public Client getClientProfileById(UUID clientId) {

        return clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

}
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
