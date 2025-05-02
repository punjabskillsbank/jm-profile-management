package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.service.FileService;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile Management", description = "Operations related to freelancer profile management")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    // POST mapping to save the freelancer profile data in database
    @Operation(summary = "Create a new freelancer profile", description = "Save the freelancer profile data in the database")
    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerDTO> createProfile(@Valid @RequestBody FreelancerDTO freelancerDTO) {
        Freelancer freelancer = freelancerProfileService.createFreelancerProfile(freelancerDTO);
        FreelancerDTO dto  = modelMapper.map(freelancer, FreelancerDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Upload profile photo for a freelancer",
            description = "Uploads a profile photo for a freelancer and returns the URL of the uploaded file",
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
    @PostMapping(value = "/{freelancerId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfilePhoto(
            @PathVariable UUID freelancerId,
            @RequestParam("file") MultipartFile file
    ) {
        // Upload the file and get the URL
        String fileUrl = fileService.uploadProfilePhoto(file, freelancerId.toString(), "freelancer");

        // Get the freelancer from the database
        Freelancer freelancer = freelancerProfileService.getFreelancerProfileById(freelancerId);

        // Update the profile photo URL
        freelancer.setProfilePhotoURL(fileUrl);
        freelancerProfileService.updateFreelancerProfile(freelancer);

        // Return the URL in the response
        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);
        return ResponseEntity.ok(response);
    }
}