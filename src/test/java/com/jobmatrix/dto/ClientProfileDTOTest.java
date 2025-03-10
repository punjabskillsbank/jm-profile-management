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
        ClientProfileDTO client = new ClientProfileDTO();
        client.setUserId(UUID.randomUUID()); // Assigns a random valid UUID.
        client.setPhoneNumber("+919876543210"); // Valid phone number.
        client.setBio("This is a sample bio."); // Valid bio.
        client.setCompanyName("Tech Innovations"); // Valid company name.
        client.setCompanySize("10-50"); // Valid format.
        client.setIndustry("IT");
        client.setCity("New Delhi");
        client.setState("Delhi");
        client.setPostalCode("110001"); // Valid postal code.
        client.setAddress("123, Business Street, New Delhi");

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(client);
        assertTrue(violations.isEmpty(), "DTO should be valid");
    }

    @Test
    void testNullUserId(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(null);
        clientProfileDTO.setCompanyName("ABC");

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "User_id cannot be null");
        assertEquals("user_id cannot be null. It must be linked to a user.", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPhoneNumber(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setPhoneNumber("12345"); // Invalid phone number.

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Phone number should be valid");
        assertEquals("Please enter a valid Phone Number", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankCompanyName(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(UUID.randomUUID());
        clientProfileDTO.setCompanyName(""); // Blank company name.

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Company name cannot be blank");
        assertEquals("company name can't be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testLongBio(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setBio("A".repeat(501)); //Exceeds 500 characters.

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Bio cannot exceed 500 characters");
        assertEquals("bio cannot exceed 500 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCompanySize(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setCompanySize("0-10"); // Invalid format.

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Company size should be Invalid");
        assertEquals("Invalid format", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPostalCode(){
        ClientProfileDTO clientProfileDTO = new ClientProfileDTO();
        clientProfileDTO.setUserId(UUID.randomUUID());
        clientProfileDTO.setCompanyName("ABC");
        clientProfileDTO.setPostalCode("1234");

        Set<ConstraintViolation<ClientProfileDTO>> violations = validator.validate(clientProfileDTO);
        assertFalse(violations.isEmpty(), "Postal code should be Invalid");
        assertEquals("Please enter a valid postal code", violations.iterator().next().getMessage());
    }

}