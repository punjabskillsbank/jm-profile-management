package com.jobmatrix.exceptionHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.controller.ClientProfileController;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(ClientProfileController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientProfileService clientProfileService;

    @Test
    void shouldReturnValidationErrors_whenInvalidInputGiven() throws Exception {

        //Create an invalid ClientDTO object with missing fields
        ClientDTO InvalidClientProfileDTO = ClientDTO.builder()
                .clientId(UUID.randomUUID())
                .phoneNumber("12345")
                .bio("Sample Bio")
                .profilePhotoURL("image.jpg")
                .companyName("")
                .companySize("0-50")
                .industry("Software Development")
                .timeZone("Asia/Kolkata")
                .city("Bangalore")
                .state("Karnataka")
                .country("India")
                .postalCode("560001")
                .address("123, MG Road, Bangalore, Karnataka, India")
                .build();

        //Send a POST request with Invalid JSON
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(InvalidClientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").exists());

    }

}