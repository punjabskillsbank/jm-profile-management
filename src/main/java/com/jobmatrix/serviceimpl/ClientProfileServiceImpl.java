package com.jobmatrix.serviceimpl;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import com.jobmatrix.exceptionHandling.ClientNotFoundException;
import com.jobmatrix.repository.ClientProfileRepository;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FileService;
import com.jobmatrix.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final ModelMapper modelMapper;
    private final S3Service s3Service;
    private final FileService fileService;


    @Transactional
    @Override
    public Client saveClientProfile(ClientDTO dto){
        Client client = modelMapper.map(dto, Client.class);
        return clientProfileRepository.save(client);
    }

    @Transactional
    @Override
    public Client createProfile(ClientDTO clientDTO, MultipartFile photo) {
        UUID clientId = clientDTO.getClientId();

        String profilePhotoURL = fileService.uploadProfilePhoto(photo, clientId.toString(), "client");
        clientDTO.setProfilePhotoURL(profilePhotoURL);

        Client client = modelMapper.map(clientDTO, Client.class);
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