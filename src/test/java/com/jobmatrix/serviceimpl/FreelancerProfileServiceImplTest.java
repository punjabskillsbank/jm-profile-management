package com.jobmatrix.serviceimpl;

import com.common.enums.ProfileStatus;
import com.jobmatrix.dto.FreelancerDTO;
import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FreelancerProfileServiceImplTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidFreelancerDTO() {
        UUID freelancerId = UUID.randomUUID();
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.isEmpty(), "Valid DTO should not have validation errors.");
    }

    @Test
    void testFreelancerDTO_NullFields() {
        FreelancerDTO freelancerDTO = new FreelancerDTO();  // Empty DTO with null fields

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("freelancerId cannot be null.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("title cannot be null.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("bio cannot be null.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("hourlyRate cannot be null.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("profileStatus cannot be null.")));
    }

    @Test
    void testFreelancerDTO_InvalidHourlyRate() {
        UUID freelancerId = UUID.randomUUID();
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId)
                .toBuilder()
                .hourlyRate(-10.0)  // Invalid hourly rate
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("hourlyRate must be greater than 0.")));
    }

    @Test
    void testFreelancerDTO_InvalidPostalCode() {
        UUID freelancerId = UUID.randomUUID();
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId)
                .toBuilder()
                .postalCode("1234A")  // Invalid format
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please enter a valid postal code")));
    }

    @Test
    void testFreelancerDTO_InvalidPhoneNumber() {
        UUID freelancerId = UUID.randomUUID();
        FreelancerDTO freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId)
                .toBuilder()
                .phoneNumber("12345")  // Invalid phone number
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please enter a valid Phone Number")));
    }
}
