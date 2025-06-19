package com.jobmatrix.exceptionHandling;

import com.common.exceptionHandling.ClientNotFoundException;
import com.common.exceptionHandling.FreelancerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.controller.ClientProfileController;
import com.jobmatrix.controller.FreelancerProfileController;
import com.jobmatrix.dto.ClientDTO;
import com.common.dto.FreelancerDTO;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import com.jobmatrix.service.ClientProfileService;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
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

@WebMvcTest(controllers = {FreelancerProfileController.class, ClientProfileController.class})
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
    private final UUID USER_ID = UUID.randomUUID(); // For UserNotFoundException
    private final Long CATEGORY_ID = 123L; // For CategoryNotFound

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
    void shouldReturnCategoryNotFoundException_whenCategoryNotFound() throws Exception {
        // Mock the service method called by POST /api/freelancer/create_profile
        Mockito.when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenThrow(new CategoryNotFound(CATEGORY_ID));

        // Create a valid FreelancerDTO to send in the request body
        FreelancerDTO validFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound()) // Expect 404 due to CategoryNotFound
                .andExpect(MockMvcResultMatchers.content().string("Category not found with ID: " + CATEGORY_ID));
    }

    @Test
    void shouldReturnBadRequest_whenNullCategoriesOffered() throws Exception {
        // Mock the service method called by POST /api/freelancer/create_profile
        Mockito.when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenThrow(new NullCategoriesOfferedException());

        // Create a valid FreelancerDTO to send in the request body
        FreelancerDTO validFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Categories cannot be null."));
    }

    @Test
    void shouldReturnBadRequest_whenCategoriesOfferedLimitExceeded() throws Exception {
        // Mock the service method called by POST /api/freelancer/create_profile
        Mockito.when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenThrow(new CategoriesOfferedLimitExceededException());

        // Create a valid FreelancerDTO to send in the request body
        FreelancerDTO validFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFreelancerDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cannot assign more than 10 categories."));
    }

}