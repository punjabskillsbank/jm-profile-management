package com.jobmatrix.exceptionHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.controller.ClientProfileController;
import com.jobmatrix.controller.FreelancerProfileController;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
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

@WebMvcTest({ClientProfileController.class, FreelancerProfileController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientProfileService clientProfileService;

    @MockitoBean
    private FreelancerProfileService freelancerProfileService;

    private final UUID CLIENT_ID = UUID.randomUUID();
    private final UUID FREELANCER_ID = UUID.randomUUID();

    @Test
    void shouldReturnValidationErrors_whenInvalidInputGiven() throws Exception {
        // Test client validation
        ClientDTO invalidClientDTO = ClientTestDataFactory.createClientDTO(UUID.randomUUID());
        invalidClientDTO.setCompanyName("");
        invalidClientDTO.setCompanySize("0-50");
        invalidClientDTO.setPhoneNumber("12345");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClientDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").exists());

        // Test freelancer validation
        FreelancerDTO invalidFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(UUID.randomUUID());
        invalidFreelancerDTO.setTitle("");
        invalidFreelancerDTO.setHourlyRate(-10.0);
        invalidFreelancerDTO.setPhoneNumber("12345");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourlyRate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").exists());
    }

    @Test
    void shouldReturnClientNotFoundException_whenClientNotFound() throws Exception {
        Mockito.when(clientProfileService.getClientProfileById(CLIENT_ID))
                .thenThrow(new ClientNotFoundException(CLIENT_ID));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/" + CLIENT_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Client not found at given clientId: " + CLIENT_ID));
    }

    @Test
    void shouldReturnFreelancerNotFoundException_whenFreelancerNotFound() throws Exception {
        Mockito.when(freelancerProfileService.getFreelancerProfileById(FREELANCER_ID))
                .thenThrow(new FreelancerNotFoundException(FREELANCER_ID));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/freelancer/" + FREELANCER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Freelancer not found at given freelancerId: " + FREELANCER_ID));
    }
}