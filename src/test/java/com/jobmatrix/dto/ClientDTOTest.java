package com.jobmatrix.dto;

import com.jobmatrix.test_utils.factory.ClientTestDataFactory;
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

class ClientDTOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final ClientDTO CLIENT_FIXED_DTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);
    private static ClientDTO clientDTO;

    @BeforeAll
    static void setup(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        clientDTO = ClientTestDataFactory.createClientDTO(CLIENT_ID);
    }

    @AfterAll
    static void tearDown(){
        validatorFactory.close();
    }

    @Test
    void testValidClientProfileDTO() {
        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertTrue(violations.isEmpty(), "DTO should be valid");
    }

    @Test
    void testNullUserId(){
        clientDTO.setClientId(null);

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "client_id cannot be null");
        assertEquals("client_id cannot be null.", violations.iterator().next().getMessage());
        clientDTO.setClientId(CLIENT_FIXED_DTO.getClientId());
    }

    @Test
    void testInvalidPhoneNumber(){
        clientDTO.setPhoneNumber("12345"); // Invalid phone number.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "Phone number should be valid");
        assertEquals("Please enter a valid Phone Number", violations.iterator().next().getMessage());
        clientDTO.setPhoneNumber(CLIENT_FIXED_DTO.getPhoneNumber());
    }

    @Test
    void testBlankCompanyName(){
        clientDTO.setCompanyName(""); // Blank company name.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "Company name cannot be blank");
        assertEquals("company name can't be blank", violations.iterator().next().getMessage());
        clientDTO.setCompanyName(CLIENT_FIXED_DTO.getCompanyName());
    }

    @Test
    void testLongBio(){
        clientDTO.setBio("A".repeat(501)); //Exceeds 500 characters.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "Bio cannot exceed 500 characters");
        assertEquals("bio cannot exceed 500 characters", violations.iterator().next().getMessage());
        clientDTO.setBio(CLIENT_FIXED_DTO.getBio());
    }

    @Test
    void testInvalidCompanySize(){
        clientDTO.setCompanySize("0-10"); // Invalid format.

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "Company size should be Invalid");
        assertEquals("Invalid format", violations.iterator().next().getMessage());
        clientDTO.setCompanySize(CLIENT_FIXED_DTO.getCompanySize());
    }

    @Test
    void testInvalidPostalCode(){
        clientDTO.setPostalCode("1234");

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(clientDTO);
        assertFalse(violations.isEmpty(), "Postal code should be Invalid");
        assertEquals("Please enter a valid postal code", violations.iterator().next().getMessage());
        clientDTO.setPostalCode(CLIENT_FIXED_DTO.getPostalCode());
    }

}