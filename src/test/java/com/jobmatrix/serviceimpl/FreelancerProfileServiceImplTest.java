package com.jobmatrix.serviceimpl;

import com.common.enums.ProfileStatus;
import com.jobmatrix.dto.FreelancerDTO;
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
        FreelancerDTO freelancerDTO = FreelancerDTO.builder()
                .freelancerId(UUID.randomUUID())
                .title("Software Developer")
                .bio("Experienced Java Developer with expertise in Spring Boot")
                .hourlyRate(50.0)
                .address("123 Street")
                .city("New York")
                .state("NY")
                .country("USA")
                .postalCode("123456")
                .phoneNumber("+919876543210")
                .isAbcMember(true)
                .profilePhotoURL("http://example.com/photo.jpg")
                .profileStatus(ProfileStatus.APPROVED)
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertTrue(violations.isEmpty(), "Valid DTO should not have validation errors.");
    }

    @Test
    void testFreelancerDTO_NullFields() {
        FreelancerDTO freelancerDTO = new FreelancerDTO();

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
        FreelancerDTO freelancerDTO = FreelancerDTO.builder()
                .freelancerId(UUID.randomUUID())
                .title("Software Developer")
                .bio("Experienced Java Developer")
                .hourlyRate(-10.0)  // Invalid: Should be positive
                .profileStatus(ProfileStatus.APPROVED)
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("hourlyRate must be greater than 0.")));
    }

    @Test
    void testFreelancerDTO_InvalidPostalCode() {
        FreelancerDTO freelancerDTO = FreelancerDTO.builder()
                .freelancerId(UUID.randomUUID())
                .title("Software Developer")
                .bio("Experienced Java Developer")
                .hourlyRate(50.0)
                .postalCode("1234A")  // Invalid format
                .profileStatus(ProfileStatus.APPROVED)
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please enter a valid postal code")));
    }

    @Test
    void testFreelancerDTO_InvalidPhoneNumber() {
        FreelancerDTO freelancerDTO = FreelancerDTO.builder()
                .freelancerId(UUID.randomUUID())
                .title("Software Developer")
                .bio("Experienced Java Developer")
                .hourlyRate(50.0)
                .phoneNumber("12345")  // Invalid: Doesn't match pattern
                .profileStatus(ProfileStatus.APPROVED)
                .build();

        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Please enter a valid Phone Number")));
    }
}
