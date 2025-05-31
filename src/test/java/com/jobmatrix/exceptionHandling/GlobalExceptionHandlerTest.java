package com.jobmatrix.exceptionHandling;

import com.common.dto.FreelancerDTO;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.common.exceptionHandling.ClientNotFoundException;
import com.jobmatrix.dto.ClientDTO;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {com.jobmatrix.controller.FreelancerProfileController.class, com.jobmatrix.controller.ClientProfileController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModelMapper modelMapper;


    @MockitoBean
    private ClientProfileService clientProfileService;

    @MockitoBean
    private FreelancerProfileService freelancerProfileService;

    private final UUID CLIENT_ID = UUID.randomUUID();
    private final UUID FREELANCER_ID= UUID.randomUUID();

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
    void shouldReturnFreelancerNotFoundException_whenFreelancerNotFound() throws Exception {
        Mockito.when(freelancerProfileService.getFreelancerProfileById(FREELANCER_ID))
                .thenThrow(new FreelancerNotFoundException(FREELANCER_ID));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/freelancer/" + FREELANCER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Freelancer not found with ID: " + FREELANCER_ID));
    }

    @Test
    void shouldReturnBadRequest_whenNullServicesProvided() throws Exception {
        Mockito.when(freelancerProfileService.saveFreelancerProfile(Mockito.any()))
                .thenThrow(new NullServicesOfferedException());

        FreelancerDTO invalidFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        invalidFreelancerDTO.setServices(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.services").value("services cannot be null."));
    }

    @Test
    void shouldReturnBadRequest_whenServiceLimitExceeded() throws Exception {
        Mockito.when(freelancerProfileService.saveFreelancerProfile(Mockito.any()))
                .thenThrow(new ServicesOfferedLimitExceededException());

        FreelancerDTO invalidFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        List<Long> tooManyServices = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tooManyServices.add((long) i);
        }
        invalidFreelancerDTO.setServices(tooManyServices);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.services").value("Cannot assign more than 10 services."));
    }

}