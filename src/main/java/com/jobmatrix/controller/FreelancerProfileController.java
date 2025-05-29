package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;
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

    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerProfileCreationResponse> initiateProfileCreation(
            @Valid @RequestBody FreelancerDTO freelancerDTO,
            @RequestParam String contentType
    ) {
        FreelancerProfileCreationResponse result = freelancerProfileService.initiateProfileCreation(freelancerDTO, contentType);
        return ResponseEntity.ok(result);
    }

}