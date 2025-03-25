package com.jobmatrix.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobDTOTest {

    private static final Long TEST_JOB_ID = 1L;
    private static final String TEST_JOB_TITLE = "Software Engineer";
    private static final String TEST_COMPANY_NAME = "TechCorp";
    private static final Date TEST_START_DATE = Date.valueOf("2022-01-01");
    private static final Date TEST_END_DATE = Date.valueOf("2023-01-01");
    private static final String TEST_JOB_RESPONSIBILITIES = "Java development";
    private static final UUID TEST_USER_ID = UUID.randomUUID();

    @Mock
    private UUID mockUserId;

    private JobDTO jobDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobDTO = new JobDTO();
    }

    @Test
    @DisplayName("Test No-Args Constructor")
    void testNoArgsConstructor() {
        // Arrange & Act
        JobDTO emptyJobDTO = new JobDTO();

        // Assert
        assertNotNull(emptyJobDTO);
        assertNull(emptyJobDTO.getJobId());
        assertNull(emptyJobDTO.getJobTitle());
        assertNull(emptyJobDTO.getCompanyName());
        assertNull(emptyJobDTO.getStartDate());
        assertNull(emptyJobDTO.getEndDate());
        assertNull(emptyJobDTO.getJobResponsibilities());
        assertNull(emptyJobDTO.getUserId());
    }

    @Test
    @DisplayName("Test All-Args Constructor")
    void testAllArgsConstructor() {
        // Arrange & Act
        JobDTO jobDTO = new JobDTO(
                TEST_JOB_ID,
                TEST_JOB_TITLE,
                TEST_COMPANY_NAME,
                TEST_START_DATE,
                TEST_END_DATE,
                TEST_JOB_RESPONSIBILITIES,
                TEST_USER_ID
        );

        // Assert
        assertEquals(TEST_JOB_ID, jobDTO.getJobId());
        assertEquals(TEST_JOB_TITLE, jobDTO.getJobTitle());
        assertEquals(TEST_COMPANY_NAME, jobDTO.getCompanyName());
        assertEquals(TEST_START_DATE, jobDTO.getStartDate());
        assertEquals(TEST_END_DATE, jobDTO.getEndDate());
        assertEquals(TEST_JOB_RESPONSIBILITIES, jobDTO.getJobResponsibilities());
        assertEquals(TEST_USER_ID, jobDTO.getUserId());
    }

    @Test
    @DisplayName("Test JobId Getter and Setter")
    void testJobIdGetterAndSetter() {
        // Act
        jobDTO.setJobId(TEST_JOB_ID);

        // Assert
        assertEquals(TEST_JOB_ID, jobDTO.getJobId());
    }

    @Test
    @DisplayName("Test JobTitle Getter and Setter")
    void testJobTitleGetterAndSetter() {
        // Act
        jobDTO.setJobTitle(TEST_JOB_TITLE);

        // Assert
        assertEquals(TEST_JOB_TITLE, jobDTO.getJobTitle());
    }

    @Test
    @DisplayName("Test CompanyName Getter and Setter")
    void testCompanyNameGetterAndSetter() {
        // Act
        jobDTO.setCompanyName(TEST_COMPANY_NAME);

        // Assert
        assertEquals(TEST_COMPANY_NAME, jobDTO.getCompanyName());
    }

    @Test
    @DisplayName("Test StartDate Getter and Setter")
    void testStartDateGetterAndSetter() {
        // Act
        jobDTO.setStartDate(TEST_START_DATE);

        // Assert
        assertEquals(TEST_START_DATE, jobDTO.getStartDate());
    }

    @Test
    @DisplayName("Test EndDate Getter and Setter")
    void testEndDateGetterAndSetter() {
        // Act
        jobDTO.setEndDate(TEST_END_DATE);

        // Assert
        assertEquals(TEST_END_DATE, jobDTO.getEndDate());
    }

    @Test
    @DisplayName("Test JobResponsibilities Getter and Setter")
    void testJobResponsibilitiesGetterAndSetter() {
        // Act
        jobDTO.setJobResponsibilities(TEST_JOB_RESPONSIBILITIES);

        // Assert
        assertEquals(TEST_JOB_RESPONSIBILITIES, jobDTO.getJobResponsibilities());
    }

    @Test
    @DisplayName("Test UserId Getter and Setter")
    void testUserIdGetterAndSetter() {
        // Act
        jobDTO.setUserId(TEST_USER_ID);

        // Assert
        assertEquals(TEST_USER_ID, jobDTO.getUserId());
    }

    @Test
    @DisplayName("Test UserId with Mocked UUID")
    void testUserIdWithMockedUUID() {
        // Act
        jobDTO.setUserId(mockUserId);

        // Assert
        assertEquals(mockUserId, jobDTO.getUserId());
        verifyNoInteractions(mockUserId); // Verify the mock was not interacted with
    }

    @Test
    @DisplayName("Test Boundary Values for Dates")
    void testBoundaryValuesForDates() {
        // Use LocalDate for better control over date values
        LocalDate minLocalDate = LocalDate.of(1, 1, 1);      // Year 0001-01-01
        LocalDate maxLocalDate = LocalDate.of(9999, 12, 31); // Year 9999-12-31

        // Convert LocalDate to SQL Date
        Date minDate = Date.valueOf(minLocalDate);
        Date maxDate = Date.valueOf(maxLocalDate);

        jobDTO.setStartDate(minDate);
        jobDTO.setEndDate(maxDate);

        assertEquals(minDate, jobDTO.getStartDate(), "Start date should be minimum boundary value");
        assertEquals(maxDate, jobDTO.getEndDate(), "End date should be maximum boundary value");
    }
}