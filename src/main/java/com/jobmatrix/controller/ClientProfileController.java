package com.jobmatrix.controller;

import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.dto.ClientDTO;
import com.common.entity.Client;
import com.jobmatrix.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientProfileById(@PathVariable UUID clientId){
        Client client = clientProfileService.getClientProfileById(clientId);
        return ResponseEntity.ok(client);
    }

    @PatchMapping("/{client_id}")
    public ResponseEntity<Client> updateClientProfile(
            @PathVariable UUID client_id,
            @Valid @RequestBody ClientUpdateRequest clientUpdateRequest
    ){
        Client updatedClient = clientProfileService.updateClientProfile(client_id, clientUpdateRequest);
        return ResponseEntity.ok(updatedClient);
    }


}