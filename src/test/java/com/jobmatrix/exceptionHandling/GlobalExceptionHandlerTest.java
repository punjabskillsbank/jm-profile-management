package com.jobmatrix.exceptionHandling;

import com.common.dto.FreelancerDTO;
import com.common.exceptionHandling.ClientNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ClientProfileService clientProfileService;
    @Mock
    private FreelancerProfileService freelancerProfileService;

    private final UUID CLIENT_ID = UUID.randomUUID();



    @Test
    void shouldReturnValidationErrors_whenInvalidInputGiven() throws Exception {

        //Create an invalid ClientDTO object with missing fields
        ClientDTO invalidClientDTO = ClientTestDataFactory.createClientDTO(UUID.randomUUID());
        invalidClientDTO.setCompanyName("");
        invalidClientDTO.setCompanySize("0-50");
        invalidClientDTO.setPhoneNumber("12345");

        //Send a POST request with Invalid JSON
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClientDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companySize").exists());

    }

    @Test
    void shouldReturnClientNotFoundException_whenClientNotFound() throws Exception {

        Mockito.when(clientProfileService.getClientProfileById(CLIENT_ID))
                .thenThrow(new ClientNotFoundException(CLIENT_ID));

        //Send a GET request with invalid client ID
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/" + CLIENT_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Client not found with ID: " + CLIENT_ID));
    }

    @Test
    void shouldReturnServiceLimitExceeded_whenTooManyServicesProvided() throws Exception {
        // Create a valid DTO using factory and add too many services
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(UUID.randomUUID());
        freelancerDTO.setServices(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L));

        // Mock the service to throw ServiceLimitExceededException
        Mockito.when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenThrow(new ServiceLimitExceededException());

        // Send a POST request with too many services
        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(freelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cannot assign more than 10 services."));
    }
}