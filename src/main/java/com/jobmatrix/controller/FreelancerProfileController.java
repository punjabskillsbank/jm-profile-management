package com.jobmatrix.controller;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile Management", description = "Operations related to freelancer profile management")
public class FreelancerProfileController {

    private static final Logger logger = Logger.getLogger(FreelancerProfileController.class);
    private final FreelancerProfileService freelancerProfileService;

    // POST mapping to save the freelancer profile data in database
    @Operation(summary = "Create a new freelancer profile", description = "Save the freelancer profile data in the database")
    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerDTO> createProfile(@Valid @RequestBody FreelancerDTO freelancerDTO) {
        FreelancerDTO dto = freelancerProfileService.createFreelancerProfile(freelancerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get freelancer profile by ID", description = "Retrieve freelancer profile information using freelancer ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Freelancer profile retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the provided ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerDTO> getFreelancerProfile(@PathVariable UUID freelancerId) {
        logger.info("Received request to get freelancer profile with ID: " + freelancerId);
        try {
            FreelancerDTO freelancer = freelancerProfileService.getFreelancerProfile(freelancerId);
            logger.info("Successfully retrieved freelancer profile: " + freelancer);
            return ResponseEntity.ok(freelancer);
        } catch (Exception e) {
            logger.error("Error processing request: " + e.getMessage(), e);
            throw e;
        }
    }
}
