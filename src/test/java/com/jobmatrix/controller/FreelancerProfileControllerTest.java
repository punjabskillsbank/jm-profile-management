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
    private FreelancerDTO freelancerDTO;
    private Freelancer savedFreelancer;

    @BeforeEach
    void setUp() {

        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        savedFreelancer = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
    }

    @Test
    void initiateProfileCreationTest() throws Exception {

        // Mock service behavior (assuming initiateProfileCreation returns an array with the created profile and upload URL)
        when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class))).thenReturn(freelancerDTO);

        when(modelMapper.map(savedFreelancer, FreelancerDTO.class))
                .thenReturn(freelancerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(freelancerDTO)))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoS3Key").value("profile_photos/" + FREELANCER_ID + ".jpg"));
    }

    @Test
    void getFreelancerByIdTest() throws Exception {

        Mockito.when(freelancerProfileService.getFreelancerProfileById(FREELANCER_ID))
                .thenReturn(freelancerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/freelancer/" + FREELANCER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoS3Key").value("profile_photos/" + FREELANCER_ID + ".jpg"));
    }
}
