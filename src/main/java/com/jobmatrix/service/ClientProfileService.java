package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientProfileService {
    ClientDTO saveClientProfile(ClientDTO dto);

    Optional<ClientDTO> getClientProfileById(UUID id);

    List<ClientDTO> getAllClientProfiles();

    ClientDTO getClientProfile(UUID clientId);
}