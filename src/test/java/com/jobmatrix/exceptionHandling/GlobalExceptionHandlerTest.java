package com.jobmatrix.exceptionHandling;

import com.jobmatrix.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_Success() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        FieldError fieldError = new FieldError("freelancerDTO", "title", "Title cannot be empty");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Title cannot be empty", response.getBody().get("title"));
    }

    @Test
    void handleUserNotFoundException_Success() {
        // Arrange
        UUID freelancerId = UUID.randomUUID();
        UserNotFoundException ex = new UserNotFoundException("Freelancer not found with ID: " + freelancerId);

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleUserNotFoundException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Freelancer not found with ID: " + freelancerId, response.getBody());
    }
}