package com.jobmatrix.jm_profile_management.service;

import com.jobmatrix.jm_profile_management.dto.ClientProfileDTO;
import com.jobmatrix.jm_profile_management.entity.ClientEntity;
import com.jobmatrix.jm_profile_management.repository.ClientProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ClientProfileServiceImpl implements ClientProfileService{

    private final ClientProfileRepository clientProfileRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ClientProfileServiceImpl(ClientProfileRepository clientProfileRepository) {
        this.clientProfileRepository = clientProfileRepository;
    }

    public ClientEntity saveClientProfile(ClientProfileDTO dto){

        if(dto.getUser_id() == null){
            throw new IllegalArgumentException("sub cannot be null. It must be linked to a user");
        }
        ClientEntity client = modelMapper.map(dto, ClientEntity.class);

        client.setCreated_at(LocalDateTime.now());
        client.setUpdated_at(LocalDateTime.now());
        return clientProfileRepository.save(client);
    }
}
