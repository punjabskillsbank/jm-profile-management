package com.jobmatrix.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class FreelancerServicesDTOTest {

    private Validator validator;
    private UUID testFreelancerId;
    private Long testCategoryId;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testFreelancerId = UUID.randomUUID();
        testCategoryId = 1L;
    }

    @Test
    public void testValidFreelancerServicesDTO() {
        FreelancerServicesDTO dto = FreelancerServicesDTO.builder()
                .freelancerId(testFreelancerId)
                .categoryId(testCategoryId)
                .build();

        Set<ConstraintViolation<FreelancerServicesDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should not have any validation errors");
    }

    @Test
    public void testNullFreelancerId() {
        FreelancerServicesDTO dto = FreelancerServicesDTO.builder()
                .categoryId(testCategoryId)
                .build();

        Set<ConstraintViolation<FreelancerServicesDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Should have one validation error for null freelancerId");
        assertEquals("freelancer_id cannot be null.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullCategoryId() {
        FreelancerServicesDTO dto = FreelancerServicesDTO.builder()
                .freelancerId(testFreelancerId)
                .build();

        Set<ConstraintViolation<FreelancerServicesDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Should have one validation error for null categoryId");
        assertEquals("category_id cannot be null.", violations.iterator().next().getMessage());
    }

    @Test
    public void testAllNullFields() {
        FreelancerServicesDTO dto = new FreelancerServicesDTO();

        Set<ConstraintViolation<FreelancerServicesDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size(), "Should have two validation errors for null fields");
    }

    @Test
    public void testGettersSetters() {
        FreelancerServicesDTO dto = new FreelancerServicesDTO();
        dto.setFreelancerId(testFreelancerId);
        dto.setCategoryId(testCategoryId);

        assertEquals(testFreelancerId, dto.getFreelancerId(), "FreelancerId should match");
        assertEquals(testCategoryId, dto.getCategoryId(), "CategoryId should match");
    }

    @Test
    public void testNoArgsConstructor() {
        FreelancerServicesDTO dto = new FreelancerServicesDTO();
        assertNotNull(dto, "No-args constructor should work");
    }

    @Test
    public void testAllArgsConstructor() {
        FreelancerServicesDTO dto = new FreelancerServicesDTO(testFreelancerId, testCategoryId);
        assertEquals(testFreelancerId, dto.getFreelancerId(), "FreelancerId should match");
        assertEquals(testCategoryId, dto.getCategoryId(), "CategoryId should match");
    }
}
