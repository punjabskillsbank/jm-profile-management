package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
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
        return new ResponseEntity<>(freelancerProfileService.saveFreelancerProfile(freelancerDTO), HttpStatus.CREATED);
    }

    //GET mapping for the freelancer
    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerDTO> getFreelancerProfileById(@PathVariable UUID freelancerId) {
        FreelancerDTO freelancer = freelancerProfileService.getFreelancerProfileById(freelancerId);
        return ResponseEntity.ok(freelancer);
    }
}
