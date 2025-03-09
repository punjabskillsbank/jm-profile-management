package com.jobmatrix.controller;

import com.jobmatrix.entity.ClientEntity;
import com.jobmatrix.dto.ClientProfileDTO;
import com.jobmatrix.service.ClientProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientProfileController {

    private final ClientProfileService service;

    @Autowired
    public ClientProfileController(ClientProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClientEntity> createClientProfile(@Valid @RequestBody ClientProfileDTO dto){
        ClientEntity savedClient = service.saveClientProfile(dto);
        System.out.println("Client created successfully");
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

}
