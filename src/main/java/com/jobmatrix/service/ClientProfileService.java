package com.jobmatrix.service;



import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.ClientEntity;

public interface ClientProfileService {
    ClientEntity saveClientProfile(ClientDTO dto);
}
