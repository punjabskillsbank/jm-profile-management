package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(FreelancerProfileController.class)
class FreelancerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FreelancerProfileService freelancerProfileService; // Mock the service layer

    private final UUID FREELANCER_ID = UUID.randomUUID();

    @Test
    void createFreelancerProfileTest() throws Exception {
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        // Mock service behavior (assuming create returns the created profile)
        Mockito.when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenReturn(freelancerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(freelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.freelancerId").value(FREELANCER_ID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Senior Software Engineer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Experienced Java and Spring Boot developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourlyRate").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123, MG Road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Bangalore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("Karnataka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("India"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode").value("560001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAbcMember").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileStatus").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.educations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobs").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.certificates").isArray());
    }
}
