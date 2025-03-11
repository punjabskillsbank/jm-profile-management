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
        ClientDTO InvalidClientProfileDTO = new ClientDTO();
        InvalidClientProfileDTO.setUserId(UUID.randomUUID());
        InvalidClientProfileDTO.setPhoneNumber("12345");
        InvalidClientProfileDTO.setBio("Sample Bio");
        InvalidClientProfileDTO.setProfilePhotoURL("image.jpg");
        InvalidClientProfileDTO.setCompanyName("");
        InvalidClientProfileDTO.setCompanySize("0-50");
        InvalidClientProfileDTO.setIndustry("Software Development");
        InvalidClientProfileDTO.setTimeZone("Asia/Kolkata");
        InvalidClientProfileDTO.setCity("Bangalore");
        InvalidClientProfileDTO.setState("Karnataka");
        InvalidClientProfileDTO.setCountry("India");
        InvalidClientProfileDTO.setPostalCode("560001");
        InvalidClientProfileDTO.setAddress("123, MG Road, Bangalore, Karnataka, India");

        //Send a POST request with Invalid JSON
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(InvalidClientProfileDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").exists());

    }

}