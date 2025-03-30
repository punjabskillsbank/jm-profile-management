package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.UUID;

public interface ClientProfileService {
<<<<<<< HEAD
    ClientDTO saveClientProfile(ClientDTO dto);

    Optional<ClientDTO> getClientProfileById(UUID id);

    List<ClientDTO> getAllClientProfiles();

    ClientDTO getClientProfile(UUID clientId);
=======
    Client saveClientProfile(ClientDTO dto);
    Client getClientProfileById(UUID clientId);
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
}