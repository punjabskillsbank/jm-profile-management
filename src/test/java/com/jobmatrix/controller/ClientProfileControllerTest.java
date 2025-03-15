package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.entity.Client;
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

    private final UUID clientId = UUID.randomUUID();

    @Test
    void createClientProfileTest() throws Exception {


        ClientDTO clientProfileDTO = ClientDTO.builder()
                .clientId(clientId)
                .phoneNumber("+919876543210")
                .bio("Experienced client")
                .profilePhotoURL("https://example.com/profile.jpg")
                .companyName("Tech Innovators Pvt Ltd")
                .companySize("10-50")
                .industry("Software Development")
                .timeZone("Asia/Kolkata")
                .city("Bangalore")
                .state("Karnataka")
                .country("India")
                .postalCode("560001")
                .address("123, MG Road, Bangalore, Karnataka, India")
                .build();

        Client clientEntity = Client.builder()
                .clientId(clientId)
                .phoneNumber("+919876543210")
                .bio("Experienced client")
                .profilePhotoURL("https://example.com/profile.jpg")
                .companyName("Tech Innovators Pvt Ltd")
                .companySize("10-50")
                .industry("Software Development")
                .timeZone("Asia/Kolkata")
                .city("Bangalore")
                .state("Karnataka")
                .country("India")
                .postalCode("560001")
                .address("123, MG Road, Bangalore, Karnataka, India")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        // Mock service behavior (assuming save returns the saved profile)
        Mockito.when(clientProfileService.saveClientProfile(Mockito.any(ClientDTO.class)))
                .thenReturn(clientEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(clientId.toString()))
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists()) // Ensures created_at is present
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists()); // Ensures updated_at is present

    }
}
