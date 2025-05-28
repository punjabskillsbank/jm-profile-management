package com.jobmatrix.controller;

import com.common.dto.FreelancerDTO;
import com.common.entity.Freelancer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FreelancerProfileController.class)

class FreelancerProfileControllerTest {

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FreelancerProfileService freelancerProfileService; // Mock the service layer

    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer savedFreelancer;
    private FreelancerDTO mappedResponseDTO;
    private UUID freelancerId;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    void initializeFreelancerTestData() {
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        savedFreelancer = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
        mappedResponseDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
    }

    @Test
    void createFreelancerProfileTest() throws Exception {
        // Mock the service to create freelancer profile
        when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenReturn(savedFreelancer);

        // Mock the service to return the complete DTO with services
        when(freelancerProfileService.getFreelancerProfileById(FREELANCER_ID))
                .thenReturn(FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID));

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.freelancerId").value(FREELANCER_ID.toString()))
                .andExpect(jsonPath("$.title").value("Senior Software Engineer"))
                .andExpect(jsonPath("$.bio").value("Experienced Java and Spring Boot developer"))
                .andExpect(jsonPath("$.hourlyRate").value(50.0))
                .andExpect(jsonPath("$.address").value("123, MG Road"))
                .andExpect(jsonPath("$.city").value("Bangalore"))
                .andExpect(jsonPath("$.state").value("Karnataka"))
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.postalCode").value("560001"))
                .andExpect(jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(jsonPath("$.isAbcMember").value(true))
                .andExpect(jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.profileStatus").value("APPROVED"))
                .andExpect(jsonPath("$.educations").isArray())
                .andExpect(jsonPath("$.jobs").isArray())
                .andExpect(jsonPath("$.certificates").isArray());
    }

    private final UUID TEST_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        freelancerId = TEST_ID;
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
    }

    @Test
    void getFreelancerByIdTest() throws Exception {

        Mockito.when(freelancerProfileService.getFreelancerProfileById(freelancerId))
                .thenReturn(freelancerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/freelancer/" + freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.freelancerId").value(freelancerId.toString()))
                .andExpect(jsonPath("$.title").value("Senior Software Engineer"))
                .andExpect(jsonPath("$.bio").value("Experienced Java and Spring Boot developer"))
                .andExpect(jsonPath("$.hourlyRate").value(50.0))
                .andExpect(jsonPath("$.address").value("123, MG Road"))
                .andExpect(jsonPath("$.city").value("Bangalore"))
                .andExpect(jsonPath("$.state").value("Karnataka"))
                .andExpect(jsonPath("$.country").value("India"))
                .andExpect(jsonPath("$.postalCode").value("560001"))
                .andExpect(jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(jsonPath("$.isAbcMember").value(true))
                .andExpect(jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"));
    }
}
