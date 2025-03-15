package com.jobmatrix.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientProfileDTOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setup(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown(){
        validatorFactory.close();
    }

    @Test
    void testValidClientProfileDTO() {
        ClientDTO client = ClientDTO.builder()
                .clientId(UUID.randomUUID()) // Assigns a random valid UUID.
                .phoneNumber("+919876543210") // Valid phone number.
                .bio("This is a sample bio.") // Valid bio.
                .companyName("Tech Innovations") // Valid company name.
                .companySize("10-50") // Valid format.
                .industry("IT")
                .city("New Delhi")
                .state("Delhi")
                .postalCode("110001") // Valid postal code.
                .address("123, Business Street, New Delhi")
                .build();

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(client);
        assertTrue(violations.isEmpty(), "DTO should be valid");
    }

    @Test
    void testNullUserId(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(null);
        clientProfileDTO.setCompanyName("ABC");

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "User_id cannot be null");
        assertEquals("client_id cannot be null.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPhoneNumber(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setPhoneNumber("12345"); // Invalid phone number.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Phone number should be valid");
        assertEquals("Please enter a valid Phone Number", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankCompanyName(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(UUID.randomUUID());
        clientProfileDTO.setCompanyName(""); // Blank company name.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Company name cannot be blank");
        assertEquals("company name can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testLongBio(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setBio("A".repeat(501)); //Exceeds 500 characters.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Bio cannot exceed 500 characters");
        assertEquals("bio cannot exceed 500 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCompanySize(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setCompanySize("0-10"); // Invalid format.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Company size should be Invalid");
        assertEquals("Invalid format", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPostalCode(){
        ClientDTO clientProfileDTO = new ClientDTO();
        clientProfileDTO.setClientId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setPostalCode("1234");

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Postal code should be Invalid");
        assertEquals("Please enter a valid postal code", violations.iterator().next().getMessage());
    }

}