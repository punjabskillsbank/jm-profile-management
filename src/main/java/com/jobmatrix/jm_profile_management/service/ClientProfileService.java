package com.jobmatrix.jm_profile_management.service;



import com.jobmatrix.jm_profile_management.dto.ClientProfileDTO;
import com.jobmatrix.jm_profile_management.entity.ClientEntity;

public interface ClientProfileService {

    ClientEntity saveClientProfile(ClientProfileDTO dto);


}
