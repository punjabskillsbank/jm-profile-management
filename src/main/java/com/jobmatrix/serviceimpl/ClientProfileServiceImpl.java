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
import java.util.HashMap;
import java.util.Map;
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

}
