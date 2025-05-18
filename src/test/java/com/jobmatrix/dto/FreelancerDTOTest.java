//package com.jobmatrix.dto;
//
//import com.common.dto.FreelancerDTO;
//import com.jobmatrix.test_utils.factory.FreelancerTestDataFactory;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FreelancerDTOTest {
//
//    private Validator validator;
//    private FreelancerDTO freelancerDTO;
//
//    @BeforeEach
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//        UUID freelancerId = UUID.randomUUID();
//        freelancerDTO = FreelancerTestDataFactory.createFreelancerDTO(freelancerId);
//    }
//
//    // Positive Case: All valid data
//    @Test
//    public void testFreelancerDTO_Valid() {
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertTrue(violations.isEmpty(), "Validation should pass for a valid FreelancerDTO");
//    }
//
//    // Negative Case: Null freelancerId
//    @Test
//    public void testFreelancerDTO_FreelancerId_Null() {
//        freelancerDTO.setFreelancerId(null);
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for null freelancerId");
//        assertEquals("freelancerId cannot be null.", violations.iterator().next().getMessage());
//    }
//
//    // Negative Case: Null title
//    @Test
//    public void testFreelancerDTO_Title_Null() {
//        freelancerDTO.setTitle(null);
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for null title");
//        assertEquals("title cannot be null.", violations.iterator().next().getMessage());
//    }
//
//    // Negative Case: Empty title
//    @Test
//    public void testFreelancerDTO_Title_Empty() {
//        freelancerDTO.setTitle("");
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for empty title");
//    }
//
//    // Negative Case: Invalid postal code (less than 6 digits)
//    @Test
//    public void testFreelancerDTO_PostalCode_Invalid() {
//        freelancerDTO.setPostalCode("1234");
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for invalid postal code");
//        assertEquals("Please enter a valid postal code", violations.iterator().next().getMessage());
//    }
//
//    // Negative Case: Invalid phone number
//    @Test
//    public void testFreelancerDTO_PhoneNumber_Invalid() {
//        freelancerDTO.setPhoneNumber("12345");
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for invalid phone number");
//        assertEquals("Please enter a valid Phone Number", violations.iterator().next().getMessage());
//    }
//
//    // Negative Case: Null profile status
//    @Test
//    public void testFreelancerDTO_ProfileStatus_Null() {
//        freelancerDTO.setProfileStatus(null);
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for null profileStatus");
//        assertEquals("profileStatus cannot be null.", violations.iterator().next().getMessage());
//    }
//
//    // Negative Case: Negative hourly rate
//    @Test
//    public void testFreelancerDTO_HourlyRate_Negative() {
//        freelancerDTO.setHourlyRate(-10);
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for negative hourly rate");
//    }
//
//
//    // Negative Case: Invalid postal code (more than 6 digits)
//    @Test
//    public void testFreelancerDTO_PostalCode_TooLong() {
//        freelancerDTO.setPostalCode("1234567");
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for invalid postal code (too long)");
//    }
//
//    // Negative Case: Invalid phone number (not starting with 6-9)
//    @Test
//    public void testFreelancerDTO_PhoneNumber_InvalidStart() {
//        freelancerDTO.setPhoneNumber("+911234567890");
//        Set<ConstraintViolation<FreelancerDTO>> violations = validator.validate(freelancerDTO);
//        assertEquals(1, violations.size(), "Expected 1 validation error for phone number not starting with 6-9");
//    }
//}