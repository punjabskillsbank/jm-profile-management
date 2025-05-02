package com.jobmatrix.serviceimpl;

import com.common.exceptionHandeling.ClientNotFoundException;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.ClientProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public Client saveClientProfile(ClientDTO dto){
        Client client = modelMapper.map(dto, Client.class);
        return clientProfileRepository.save(client);
    }

    @Override
    public Client getClientProfileById(UUID clientId) {

        return clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
    }

    @Transactional
    @Override
    public Client updateClientProfile(UUID client_id, ClientUpdateRequest clientUpdateRequest){

        Client tempClient = clientProfileRepository.findById(client_id)
                .orElseThrow(() -> new ClientNotFoundException(client_id));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(clientUpdateRequest, tempClient);

        return clientProfileRepository.save(tempClient);
    }

}