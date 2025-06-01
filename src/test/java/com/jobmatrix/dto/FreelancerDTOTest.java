package com.jobmatrix.dto;

import com.common.dto.FreelancerDTO;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FreelancerDTOTest {

    private Validator validator;
    private FreelancerDTO freelancerDTO;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        UUID freelancerId = UUID.randomUUID();
        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
    }

    // Positive Case: All valid data
    @Test
    public void testFreelancerDTO_Valid() {
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.isEmpty(), "Validation should pass for a valid FreelancerDTO");
    }

    // Negative Case: Null freelancerId
    @Test
    public void testFreelancerDTO_FreelancerId_Null() {
        freelancerDTO.setFreelancerId(null);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 2 validation errors for null freelancerId");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("freelancerId"));
        assertTrue(containsError, "Expected error message containing 'freelancerId'");
    }

    // Negative Case: Null title
    @Test
    public void testFreelancerDTO_Title_Null() {
        freelancerDTO.setTitle(null);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 2 validation errors for null title");
        boolean containsTitleError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("title"));
        assertTrue(containsTitleError, "Expected error message containing 'title'");
    }

    // Negative Case: Empty title
    @Test
    public void testFreelancerDTO_Title_Empty() {
        freelancerDTO.setTitle("");
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 2 validation errors for empty title");
        boolean containsTitleError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("title"));
        assertTrue(containsTitleError, "Expected error message containing 'title'");
    }

    // Negative Case: Invalid postal code (less than 6 digits)
    @Test
    public void testFreelancerDTO_PostalCode_Invalid() {
        freelancerDTO.setPostalCode("1234");
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.size() > 0, "Expected validation errors for invalid postal code");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("Please enter a valid postal code"));
        assertTrue(containsError, "Expected error message 'Please enter a valid postal code'");
    }

    // Negative Case: Invalid phone number
    @Test
    public void testFreelancerDTO_PhoneNumber_Invalid() {
        freelancerDTO.setPhoneNumber("12345");
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.size() > 0, "Expected validation errors for invalid phone number");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("Please enter a valid Phone Number"));
        assertTrue(containsError, "Expected error message 'Please enter a valid Phone Number'");
    }

    // Negative Case: Null profile status
    @Test
    public void testFreelancerDTO_ProfileStatus_Null() {
        freelancerDTO.setProfileStatus(null);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.size() > 0, "Expected validation errors for null profile status");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("profileStatus cannot be null."));
        assertTrue(containsError, "Expected error message 'profileStatus cannot be null.'");
    }

    // Negative Case: Negative hourly rate
    @Test
    public void testFreelancerDTO_HourlyRate_Negative() {
        freelancerDTO.setHourlyRate(-10);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 2 validation errors for negative hourly rate");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("hourlyRate"));
        assertTrue(containsError, "Expected error message containing 'hourlyRate'");
    }

    // Negative Case: Invalid postal code (more than 6 digits)
    @Test
    public void testFreelancerDTO_PostalCode_TooLong() {
        freelancerDTO.setPostalCode("1234567");
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.size() > 0, "Expected validation errors for postal code too long");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("Please enter a valid postal code"));
        assertTrue(containsError, "Expected error message 'Please enter a valid postal code'");
    }

    // Negative Case: Invalid phone number (not starting with 6-9)
    @Test
    public void testFreelancerDTO_PhoneNumber_InvalidStart() {
        freelancerDTO.setPhoneNumber("+911234567890");
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.size() > 0, "Expected validation errors for phone number not starting with 6-9");
        boolean containsError = violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("Please enter a valid Phone Number"));
        assertTrue(containsError, "Expected error message 'Please enter a valid Phone Number'");
    }

    // Test Case: Services should not be null
    @Test
    public void testFreelancerDTO_Services_Null() {
        freelancerDTO.setServices(null);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 1 validation error for null services");
        assertEquals("Must have at least one service.", violations.iterator().next().getMessage());
    }

    // Test Case: Services should not exceed max limit
    @Test
    public void testFreelancerDTO_Services_TooMany() {
        // Create a list with more than the maximum allowed services
        List<Long> tooManyServices = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tooManyServices.add((long) (Math.random() * 10000));
        }
        freelancerDTO.setServices(tooManyServices);
        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertEquals(1, violations.size(), "Expected 1 validation error for too many services");
        assertEquals("Cannot assign more than 10 services.", violations.iterator().next().getMessage());
    }

}