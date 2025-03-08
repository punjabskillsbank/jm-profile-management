package com.jobmatrix.jm_profile_management.serviceImpl;

import com.jobmatrix.jm_profile_management.dto.ClientProfileDTO;
import com.jobmatrix.jm_profile_management.entity.ClientEntity;
import com.jobmatrix.jm_profile_management.repository.ClientProfileRepository;
import com.jobmatrix.jm_profile_management.service.ClientProfileService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ModelMapper modelMapper;

    public ClientProfileServiceImpl(ClientProfileRepository clientProfileRepository, ModelMapper modelMapper) {
        this.clientProfileRepository = clientProfileRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ClientEntity saveClientProfile(ClientProfileDTO dto){

        if(dto.getUser_id() == null){
            throw new IllegalArgumentException("user_id cannot be null. It must be linked to a user");
        }
        ClientEntity client = modelMapper.map(dto, ClientEntity.class);

        client.setCreated_at(LocalDateTime.now());
        client.setUpdated_at(LocalDateTime.now());
        return clientProfileRepository.save(client);
    }
}
