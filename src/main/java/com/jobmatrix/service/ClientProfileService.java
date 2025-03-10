package com.jobmatrix.service;



import com.jobmatrix.dto.ClientProfileDTO;
import com.jobmatrix.entity.ClientEntity;

public interface ClientProfileService {
    ClientEntity saveClientProfile(ClientProfileDTO dto);
}
