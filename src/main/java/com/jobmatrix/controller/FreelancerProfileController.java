package com.jobmatrix.controller;

import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer")
@RequiredArgsConstructor
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;

    // POST mapping to save the freelancer profile data in database
    @PostMapping("/create_profile")
    public ResponseEntity<FreelancerDTO> createProfile(@RequestBody FreelancerDTO freelancerDTO) {
        FreelancerDTO dto = freelancerProfileService.createFreelancerProfile(freelancerDTO);
        return ResponseEntity.ok(dto);
    }

}
