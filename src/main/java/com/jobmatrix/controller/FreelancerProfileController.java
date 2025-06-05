package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.common.dto.ProfileVisibilityDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile Management", description = "Operations related to freelancer profile management")
@Slf4j
public class FreelancerProfileController {
    private final FreelancerProfileService freelancerProfileService;
    private final ModelMapper modelMapper;

    // POST mapping to save the freelancer profile data in database
    @Operation(summary = "Create a new freelancer profile", description = "Save the freelancer profile data in the database")
    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerDTO> createProfile(@Valid @RequestBody FreelancerDTO freelancerDTO) {
        FreelancerDTO dto  = freelancerProfileService.createFreelancerProfile(freelancerDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //GET mapping for the freelancer
    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerDTO> getFreelancerProfileById(@PathVariable UUID freelancerId) {
        FreelancerDTO freelancer = freelancerProfileService.getFreelancerProfileById(freelancerId);
        return ResponseEntity.ok(freelancer);
    }

    //Patch mapping to update profile visibility
    @PatchMapping("/update-visibility")
    @Operation(summary = "Update profile visibility", description = "Update the visibility of a freelancer's profile")
    public ResponseEntity<FreelancerDTO> updateProfileVisibility(@Valid @RequestBody ProfileVisibilityDTO dto ) {
        FreelancerDTO updatedFreelancer = freelancerProfileService.updateProfileVisibility(dto);
        return ResponseEntity.ok(updatedFreelancer);
    }

}
