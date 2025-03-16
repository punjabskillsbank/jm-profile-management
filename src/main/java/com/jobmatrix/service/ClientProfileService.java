package com.jobmatrix.service;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;

public interface ClientProfileService {
    Client saveClientProfile(ClientDTO dto);
}