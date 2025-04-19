package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile Management", description = "Operations related to freelancer profile management")
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

}
