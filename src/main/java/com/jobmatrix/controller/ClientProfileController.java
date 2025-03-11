package com.jobmatrix.controller;

import com.jobmatrix.entity.ClientEntity;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @PostMapping("/create_client")
    public ResponseEntity<ClientEntity> createClientProfile(@Valid @RequestBody ClientDTO dto){
        ClientEntity savedClient = clientProfileService.saveClientProfile(dto);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

}
