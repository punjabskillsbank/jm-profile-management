package com.jobmatrix.dto;

import com.common.dto.EducationDTO;
import com.jobmatrix.service.EducationService;
import com.jobmatrix.serviceimpl.EducationServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EducationDTOTest {

    private Validator validator;
    private EducationDTO educationDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        educationDTO = new EducationDTO(
                "B.Tech",
                "Computer Science",
                "Thapar University",
                2015,
                2019,
                UUID.randomUUID()
        );
    }

    // ✅ Valid Case
    @Test
    void testValidEducationDTO() {
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);
        assertTrue(violations.isEmpty(), "No validation errors expected for valid EducationDTO.");
    }

    //  degree should not be blank
    @Test
    void testDegree_Blank() {
        educationDTO.setDegree(" ");
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for blank degree.");
        assertEquals("degree cannot be blank.", violations.iterator().next().getMessage());
    }

    //  degree exceeding max length (100 chars)
    @Test
    void testDegree_MaxLengthExceeded() {
        educationDTO.setDegree("A".repeat(101));
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for exceeding max length of degree.");
        assertEquals("degree must be at most 100 characters.", violations.iterator().next().getMessage());
    }

    //  specialization should not be blank
    @Test
    void testSpecialization_Blank() {
        educationDTO.setSpecialization(" ");
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for blank specialization.");
        assertEquals("specialization cannot be blank.", violations.iterator().next().getMessage());
    }

    //  institution should not be blank
    @Test
    void testInstitution_Blank() {
        educationDTO.setInstitution(" ");
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for blank institution.");
        assertEquals("institution cannot be blank.", violations.iterator().next().getMessage());
    }

    //  institution exceeding max length (150 chars)
    @Test
    void testInstitution_MaxLengthExceeded() {
        educationDTO.setInstitution("A".repeat(151));
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for exceeding max length of institution.");
        assertEquals("institution name must be at most 150 characters.", violations.iterator().next().getMessage());
    }

    //  startYear should not be null
    @Test
    void testStartYear_Null() {
        educationDTO.setStartYear(null);
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for null startYear.");
        assertEquals("startYear cannot be null.", violations.iterator().next().getMessage());
    }

    //  startYear should not be before 1950
    @Test
    void testStartYear_BelowMinimum() {
        educationDTO.setStartYear(1949);
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for startYear below minimum.");
        assertEquals("startYear must be after 1950.", violations.iterator().next().getMessage());
    }

    //  endYear should not be null
    @Test
    void testEndYear_Null() {
        educationDTO.setEndYear(null);
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for null endYear.");
        assertEquals("endYear cannot be null.", violations.iterator().next().getMessage());
    }

//
//    //  endYear should not be before startYear
//    @Test
//    void testEndYear_BeforeStartYear() {
//        educationDTO.setStartYear(2015);
//        educationDTO.setEndYear(2010);
//        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);
//
//        assertEquals(1, violations.size(), "Expected 1 validation error for endYear before startYear.");
//    }

    @Test
    void testCreateEducation_InvalidYears() {
        EducationService educationService = new EducationServiceImpl();

        EducationDTO educationDTO = new EducationDTO();
        educationDTO.setStartYear(2015);
        educationDTO.setEndYear(2010);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            educationService.createEducation(educationDTO);
        });

        assertEquals("End year must be greater than or equal to start year.", exception.getMessage());
    }

    //  userId should not be null
    @Test
    void testUserId_Null() {
        educationDTO.setFreelancerId(null);
        Set<ConstraintViolation<EducationDTO>> violations = validator.validate(educationDTO);

        assertEquals(1, violations.size(), "Expected 1 validation error for null userId.");
        assertEquals("freelancerId cannot be null.", violations.iterator().next().getMessage());
    }
}
