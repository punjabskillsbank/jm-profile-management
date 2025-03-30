package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(ClientProfileController.class)
class ClientProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientProfileService clientProfileService;  // Mocking the service

    private final UUID CLIENT_ID = UUID.randomUUID();

    @Test
    void createClientProfileTest() throws Exception {
        // Use ClientDTO instead of Client
        ClientDTO clientProfileDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);

        // Mock service behavior to return ClientDTO
        Mockito.when(clientProfileService.saveClientProfile(Mockito.any(ClientDTO.class)))
                .thenReturn(clientProfileDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID.toString()))
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists());
    }
}
