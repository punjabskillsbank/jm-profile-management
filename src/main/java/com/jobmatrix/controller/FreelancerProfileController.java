package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.jobmatrix.dto.FreelancerProfileCreationResponse;
import com.jobmatrix.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerProfileCreationResponse> initiateProfileCreation(
            @Valid @RequestBody FreelancerDTO freelancerDTO,
            @RequestParam String contentType
    ) {
        FreelancerProfileCreationResponse result = freelancerProfileService.initiateProfileCreation(freelancerDTO, contentType);
        return ResponseEntity.ok(result);
    }

    //GET mapping for the freelancer
    @GetMapping("/{freelancerId}")
    public ResponseEntity<FreelancerDTO> getFreelancerProfileById(@PathVariable UUID freelancerId) {
        FreelancerDTO freelancer = freelancerProfileService.getFreelancerProfileById(freelancerId);
        return ResponseEntity.ok(freelancer);
    }
}
