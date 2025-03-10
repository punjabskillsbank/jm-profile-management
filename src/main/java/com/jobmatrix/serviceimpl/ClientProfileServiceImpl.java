package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.ClientProfileDTO;
import com.jobmatrix.entity.ClientEntity;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.ClientProfileService;
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

        if(dto.getUserId() == null){
            throw new IllegalArgumentException("user_id cannot be null. It must be linked to a user");
        }
        ClientEntity client = modelMapper.map(dto, ClientEntity.class);

        client.setCreated_at(LocalDateTime.now());
        client.setUpdated_at(LocalDateTime.now());
        return clientProfileRepository.save(client);
    }
}
