package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientProfileDTO;
import com.jobmatrix.entity.ClientEntity;
import com.jobmatrix.service.ClientProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

@WebMvcTest(ClientProfileController.class)
class ClientProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientProfileService clientProfileService; // Mock the service layer

    private final UUID userId = UUID.randomUUID();

    @Test
    void createClientProfileTest() throws Exception {

        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUser_id(userId);
        clientProfileDTO.setPhoneNumber("+919876543210");
        clientProfileDTO.setBio("Experienced client");
        clientProfileDTO.setProfilePhotoURL("https://example.com/profile.jpg");
        clientProfileDTO.setCompanyName("Tech Innovators Pvt Ltd");
        clientProfileDTO.setCompanySize("10-50");
        clientProfileDTO.setIndustry("Software Development");
        clientProfileDTO.setTimeZone("Asia/Kolkata");
        clientProfileDTO.setCity("Bangalore");
        clientProfileDTO.setState("Karnataka");
        clientProfileDTO.setCountry("India");
        clientProfileDTO.setPostalCode("560001");
        clientProfileDTO.setAddress("123, MG Road, Bangalore, Karnataka, India");

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setUser_id(userId);
        clientEntity.setPhoneNumber("+919876543210");
        clientEntity.setBio("Experienced client");
        clientEntity.setProfilePhotoURL("https://example.com/profile.jpg");
        clientEntity.setCompanyName("Tech Innovators Pvt Ltd");
        clientEntity.setCompanySize("10-50");
        clientEntity.setIndustry("Software Development");
        clientEntity.setTimeZone("Asia/Kolkata");
        clientEntity.setCity("Bangalore");
        clientEntity.setState("Karnataka");
        clientEntity.setCountry("India");
        clientEntity.setPostalCode("560001");
        clientEntity.setAddress("123, MG Road, Bangalore, Karnataka, India");
        clientEntity.setCreated_at(LocalDateTime.now());
        clientEntity.setUpdated_at(LocalDateTime.now());




        // Mock service behavior (assuming save returns the saved profile)
        Mockito.when(clientProfileService.saveClientProfile(Mockito.any(ClientProfileDTO.class)))
                .thenReturn(clientEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Experienced client"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("Tech Innovators Pvt Ltd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").value("10-50"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.industry").value("Software Development"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZone").value("Asia/Kolkata"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Bangalore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("Karnataka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("India"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode").value("560001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123, MG Road, Bangalore, Karnataka, India"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at").exists()) // Ensures created_at is present
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at").exists()); // Ensures updated_at is present

    }
}
