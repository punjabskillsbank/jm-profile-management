package com.jobmatrix.controller;

import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client Profile Management", description = "Operations related to client profile management")
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

<<<<<<< HEAD
    @Operation(summary = "Create a new client profile", description = "Save the client profile data in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client profile created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
=======

>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
    @PostMapping("/create_profile")
    public ResponseEntity<ClientDTO> createClientProfile(@Valid @RequestBody ClientDTO dto) {
        ClientDTO savedClient = clientProfileService.saveClientProfile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

<<<<<<< HEAD

=======
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientProfileById(@PathVariable UUID clientId){
        Client client = clientProfileService.getClientProfileById(clientId);
        return ResponseEntity.ok(client);
    }
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b

}