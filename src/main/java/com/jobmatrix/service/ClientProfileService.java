package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;                                                                                                                                                                                                                                                                                                                                                                                                                    
import org.springframework.web.multipart.MultipartFile;
import com.common.entity.Client;
import java.util.UUID;

public interface ClientProfileService {
    Client createProfile(ClientDTO clientDTO, MultipartFile photo);
    Client saveClientProfile(ClientDTO dto);
    Client getClientProfileById(UUID clientId);
    Client updateClientProfile(UUID client_id, ClientUpdateRequest clientUpdateRequest);

}