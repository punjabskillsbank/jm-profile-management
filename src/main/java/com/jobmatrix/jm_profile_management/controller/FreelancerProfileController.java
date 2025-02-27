package com.jobmatrix.jm_profile_management.controller;

import com.jobmatrix.jm_profile_management.service.FreelancerService;
import com.jobmatrix.jm_profile_management.model.FreelancerDTO;
import com.jobmatrix.jm_profile_management.service.FreelancerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FreelancerProfileController {

    private final FreelancerService freelancerService;


    public FreelancerProfileController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    // POST mapping to save the freelancer profile data in database
    @PostMapping("/freelancer")
    public FreelancerDTO createFreelancer(@RequestBody FreelancerDTO freelancerDTO) {
        return freelancerService.saveFreelancerProfile(freelancerDTO);
    }

}
