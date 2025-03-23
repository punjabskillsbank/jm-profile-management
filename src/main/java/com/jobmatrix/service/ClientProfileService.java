package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientProfileService {
    Client saveClientProfile(ClientDTO dto);
    Optional<Client> getClientProfileById(UUID clientId);
}