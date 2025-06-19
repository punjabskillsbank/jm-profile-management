package com.jobmatrix.dto;

import com.common.dto.CategoryDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDTOTest {

    private Validator validator;
    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        // Initialize with valid data for most tests
        categoryDTO = CategoryDTO.builder()
                .categoryId(1L)
                .category("Software Development")
                .speciality("Backend")
                .build();
    }

    // Positive Case: All valid data
    @Test
    public void testCategoryDTO_Valid() {
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);
        assertTrue(violations.isEmpty(), "Validation should pass for a valid CategoryDTO");
    }

    // Negative Case: Null categoryId
    @Test
    public void testCategoryDTO_CategoryId_Null() {
        categoryDTO.setCategoryId(null);
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);
        assertEquals(1, violations.size(), "Expected 1 validation error for null categoryId");
        ConstraintViolation<CategoryDTO> violation = violations.iterator().next();
        assertEquals("CategoryId cannot be null", violation.getMessage());
        assertEquals("categoryId", violation.getPropertyPath().toString());
    }
}
