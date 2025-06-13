package com.jobmatrix.controller;

import com.common.dto.CategoryDTO;
import com.common.dto.FreelancerDTO;
import com.common.dto.FreelancerServicesUpdateRequestDTO;
import com.common.entity.Freelancer;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.jobmatrix.service.FreelancerProfileService;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebMvcTest(FreelancerProfileController.class)

class FreelancerProfileControllerTest {

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FreelancerProfileService freelancerProfileService; // Mock the service layer

    private final UUID FREELANCER_ID = UUID.randomUUID();
    private FreelancerDTO inputFreelancerDTO;
    private Freelancer savedFreelancer;
    private FreelancerDTO mappedResponseDTO;
    private UUID freelancerId;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    void initializeFreelancerTestData() {
        inputFreelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
        savedFreelancer = FreelancerTestDataFactory.createFreelancerEntity(FREELANCER_ID);
        mappedResponseDTO = FreelancerTestDataFactory.createFreelancerDTO(FREELANCER_ID);
    }

    @Test
    void createFreelancerProfileTest() throws Exception {

        // Mock service behavior (assuming create returns the created profile)
        when(freelancerProfileService.createFreelancerProfile(Mockito.any(FreelancerDTO.class)))
                .thenReturn(mappedResponseDTO);

        when(modelMapper.map(savedFreelancer, FreelancerDTO.class))
                .thenReturn(mappedResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/freelancer/create_profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputFreelancerDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.freelancerId").value(FREELANCER_ID.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Senior Software Engineer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Experienced Java and Spring Boot developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourlyRate").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123, MG Road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Bangalore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("Karnataka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("India"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode").value("560001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAbcMember").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileStatus").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.educations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jobs").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.certificates").isArray());
    }

    private final UUID TEST_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        freelancerId = TEST_ID;
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
    }

    @Test
    void getFreelancerByIdTest() throws Exception {

        Mockito.when(freelancerProfileService.getFreelancerProfileById(freelancerId))
                .thenReturn(freelancerDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/freelancer/" + freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.freelancerId").value(freelancerId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Senior Software Engineer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bio").value("Experienced Java and Spring Boot developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourlyRate").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123, MG Road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Bangalore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("Karnataka"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("India"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode").value("560001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("+919876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAbcMember").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePhotoURL").value("https://example.com/profile.jpg"));
    }

    @Test
    void updateCategoriesForFreelancerIdTest() throws Exception {
        // Create test data
        Set<Long> categoryIds = new HashSet<>(Arrays.asList(3L, 4L));
        FreelancerServicesUpdateRequestDTO request = new FreelancerServicesUpdateRequestDTO(categoryIds);

        // Create expected response using the existing mappedResponseDTO
        Set<CategoryDTO> updatedCategories = new HashSet<>(Arrays.asList(
                CategoryDTO.builder().categoryId(3L).category("Frontend Development").build(),
                CategoryDTO.builder().categoryId(4L).category("ReactJS").build()
        ));
        FreelancerDTO expectedResponse = mappedResponseDTO.toBuilder()
                .categoriesDTO(updatedCategories)
                .build();

        // Mock service behavior
        when(freelancerProfileService.updateCategories(freelancerId, categoryIds))
                .thenReturn(expectedResponse);

        // Perform request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/freelancer/" + freelancerId + "/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoriesDTO[*].categoryId")
                        .value(containsInAnyOrder(3, 4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoriesDTO[*].category")
                        .value(containsInAnyOrder("Frontend Development", "ReactJS")));
    }
}
