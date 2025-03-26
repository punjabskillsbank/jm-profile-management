package com.jobmatrix.exceptionHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatrix.controller.ClientProfileController;
import com.jobmatrix.dto.ClientDTO;
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
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientProfileService clientProfileService;

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
                .andExpect(MockMvcResultMatchers.content().string("Client not found at given clientId: " + CLIENT_ID));
    }

}