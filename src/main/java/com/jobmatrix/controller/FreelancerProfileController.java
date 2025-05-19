package com.jobmatrix.controller;
import com.common.entity.Client;
import com.jobmatrix.serviceimpl.FreelancerProfileServiceImpl;
import lombok.extern.slf4j.Slf4j;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
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
        Freelancer freelancer = freelancerProfileService.createFreelancerProfile(freelancerDTO);
        FreelancerDTO dto  = modelMapper.map(freelancer, FreelancerDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);


    }

    //GET mapping for the freelancer
    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerDTO> getFreelancerProfileById(@PathVariable UUID freelancerId) {
        FreelancerDTO freelancer = freelancerProfileService.getFreelancerProfileById(freelancerId);
        return ResponseEntity.ok(freelancer);
    }

}
