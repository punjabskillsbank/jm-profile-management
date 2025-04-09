package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;

import java.util.UUID;

public interface ClientProfileService {
    Client saveClientProfile(ClientDTO dto);
    Client getClientProfileById(UUID clientId);
    Client updateClientProfile(UUID client_id, ClientUpdateRequest clientUpdateRequest);

}