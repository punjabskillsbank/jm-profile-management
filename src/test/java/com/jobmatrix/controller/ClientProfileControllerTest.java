package com.jobmatrix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.ClientUpdateRequest;
import com.jobmatrix.entity.Client;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private ClientProfileService clientProfileService; // Mock the service layer

    private final UUID CLIENT_ID = UUID.randomUUID();

    @Test
    void createClientProfileTest() throws Exception {
        Client clientEntity = ClientTestDataFactory.createClientEntity(CLIENT_ID);
        ClientDTO clientProfileDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);

        // Mock service behavior (assuming save returns the saved profile)
        Mockito.when(clientProfileService.saveClientProfile(Mockito.any(ClientDTO.class)))
                .thenReturn(clientEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists()) // Ensures created_at is present
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists()); // Ensures updated_at is present

    }

    @Test
    void getClientByIdTest() throws Exception {
        Client clientEntity = ClientTestDataFactory.createClientEntity(CLIENT_ID);
        ClientDTO clientProfileDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);

        // Mock service behavior (assuming save returns the saved profile)
        Mockito.when(clientProfileService.getClientProfileById(CLIENT_ID)).thenReturn(clientEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/" + CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect 200 OK
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists()) // Ensures created_at is present
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists()); // Ensures updated_at is present
    }

    @Test
    void updateClientProfileTest() throws Exception {

        Client updatedClientEntity = ClientTestDataFactory.createClientEntity(CLIENT_ID);

        updatedClientEntity = updatedClientEntity.toBuilder()
                .phoneNumber("+919876543210")
                .bio("Updated bio")
                .companyName("Updated Company Name Ltd")
                .build();

        ClientUpdateRequest clientUpdateRequest = ClientTestDataFactory.createClientUpdateRequest(CLIENT_ID);

        clientUpdateRequest = clientUpdateRequest.toBuilder()
                .phoneNumber("+919876543210")
                .bio("Updated bio")
                .companyName("Updated Company Name Ltd")
                .build();

        Mockito.when(clientProfileService.updateClientProfile(Mockito.eq(CLIENT_ID), Mockito.any(ClientUpdateRequest.class)))
                .thenReturn(updatedClientEntity);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/clients/" + CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Expect 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(CLIENT_ID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Updated bio"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("Updated Company Name Ltd"))
                //verify other fields remain unchanged
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").value(updatedClientEntity.getCompanySize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.industry").value(updatedClientEntity.getIndustry()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeZone").value(updatedClientEntity.getTimeZone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(updatedClientEntity.getCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(updatedClientEntity.getState()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(updatedClientEntity.getCountry()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode").value(updatedClientEntity.getPostalCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(updatedClientEntity.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists()) // Ensures created_at is present
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists()); // Ensures updated_at is present

                Mockito.verify(clientProfileService).updateClientProfile(Mockito.eq(CLIENT_ID), Mockito.any(ClientUpdateRequest.class));

    }





}