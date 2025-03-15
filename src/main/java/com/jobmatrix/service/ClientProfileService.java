    package com.jobmatrix.service;
    
    
    
    import com.jobmatrix.dto.ClientDTO;
    import com.jobmatrix.entity.Client;
    
    import java.util.UUID;
    
    public interface ClientProfileService {
        Client saveClientProfile(ClientDTO dto);
    }
