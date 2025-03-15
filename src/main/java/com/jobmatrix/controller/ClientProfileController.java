package com.jobmatrix.controller;

import com.jobmatrix.entity.Client;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @PostMapping("/create_profile")
    public ResponseEntity<Client> createClientProfile(@Valid @RequestBody ClientDTO dto){
        Client savedClient = clientProfileService.saveClientProfile(dto);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

}
