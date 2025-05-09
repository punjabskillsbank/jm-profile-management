package com.jobmatrix.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientProfileController {

    private final ClientProfileService clientProfileService;
    private final FileService fileService;

//     @PostMapping("/create_profile")
//     public ResponseEntity<Client> createClientProfile(@Valid @RequestBody ClientDTO dto){
//         Client savedClient = clientProfileService.saveClientProfile(dto);
//         return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
//     }

@PostMapping(value = "/create_profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Client> createProfile(
        @RequestPart("profile") @Valid ClientDTO clientDTO,
        @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Client result = clientProfileService.createProfile(clientDTO, photo);
        return ResponseEntity.ok(result);
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

    @Operation(
            summary = "Upload profile photo for a client",
            description = "Uploads a profile photo for a client and returns the URL of the uploaded file",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully uploaded profile photo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/{clientId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfilePhoto(
            @PathVariable UUID clientId,
            @RequestParam("file") MultipartFile file
    ) {
        // Upload the file and get the URL
        String fileUrl = fileService.uploadProfilePhoto(file, clientId.toString(), "client");

        // Update the client's profile photo URL
        ClientUpdateRequest updateRequest = new ClientUpdateRequest();
        updateRequest.setProfilePhotoURL(fileUrl);
        clientProfileService.updateClientProfile(clientId, updateRequest);

        // Return the URL in the response
        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);
        return ResponseEntity.ok(response);
    }
}