package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.exception.UserNotFoundException;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FreelancerProfileController.class)
class FreelancerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FreelancerProfileService freelancerProfileService;

    private FreelancerDTO freelancerDTO;
    private UUID freelancerId;

    @BeforeEach
    void setUp() {
        freelancerId = UUID.randomUUID();
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
    }

    @Test
    void createProfile_Success() throws Exception {
        when(freelancerProfileService.createFreelancerProfile(any(FreelancerDTO.class)))
                .thenReturn(freelancerDTO);

        mockMvc.perform(post("/api/freelancer/create_profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freelancerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.freelancerId").value(freelancerId.toString()));
    }

    @Test
    void getFreelancerProfile_Success() throws Exception {
        when(freelancerProfileService.getFreelancerProfile(freelancerId))
                .thenReturn(freelancerDTO);

        mockMvc.perform(get("/api/freelancer/{freelancerId}", freelancerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.freelancerId").value(freelancerId.toString()));
    }

    @Test
    void getFreelancerProfile_NotFound() throws Exception {
        when(freelancerProfileService.getFreelancerProfile(freelancerId))
                .thenThrow(new UserNotFoundException("Freelancer not found with ID: " + freelancerId));

        mockMvc.perform(get("/api/freelancer/{freelancerId}", freelancerId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Freelancer not found with ID: " + freelancerId));
    }
}
