package com.jobmatrix.dto;

import com.common.dto.CertificateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CertificateDTOTest {

    private CertificateDTO certificateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        certificateDTO = new CertificateDTO();
    }

    @Test
    void testDefaultConstructor() {
        CertificateDTO dto = new CertificateDTO();
        assertNull(dto.getCertificateName());
        assertNull(dto.getIssuedBy());
        assertNull(dto.getIssueDate());
        assertNull(dto.getExpiryDate());
        assertNull(dto.getCredentialUrl());
        assertNull(dto.getFreelancerId());
    }

    @Test
    void testParameterizedConstructor() {
        Long certificateId = 1L;
        String certificateName = "AWS Certified Developer";
        String issuedBy = "Amazon";
        Date issueDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        String credentialUrl = "https://example.com/cert";
        UUID userId = UUID.randomUUID();

        CertificateDTO dto = new CertificateDTO(certificateName, issuedBy, issueDate, expiryDate, credentialUrl, userId);

        assertEquals(certificateName, dto.getCertificateName());
        assertEquals(issuedBy, dto.getIssuedBy());
        assertEquals(issueDate, dto.getIssueDate());
        assertEquals(expiryDate, dto.getExpiryDate());
        assertEquals(credentialUrl, dto.getCredentialUrl());
        assertEquals(userId, dto.getFreelancerId());
    }

    @Test
    void testSettersAndGetters() {
        Long certificateId = 2L;
        String certificateName = "Google Cloud Professional";
        String issuedBy = "Google";
        Date issueDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + 730L * 24 * 60 * 60 * 1000);
        String credentialUrl = "https://google.com/cert";
        UUID userId = UUID.randomUUID();

        certificateDTO.setCertificateName(certificateName);
        certificateDTO.setIssuedBy(issuedBy);
        certificateDTO.setIssueDate(issueDate);
        certificateDTO.setExpiryDate(expiryDate);
        certificateDTO.setCredentialUrl(credentialUrl);
        certificateDTO.setFreelancerId(userId);

        assertEquals(certificateName, certificateDTO.getCertificateName());
        assertEquals(issuedBy, certificateDTO.getIssuedBy());
        assertEquals(issueDate, certificateDTO.getIssueDate());
        assertEquals(expiryDate, certificateDTO.getExpiryDate());
        assertEquals(credentialUrl, certificateDTO.getCredentialUrl());
        assertEquals(userId, certificateDTO.getFreelancerId());
    }

    @Test
    void testBoundaryConditionsForDates() {
        Date pastDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10 * 365)); // 10 years ago
        Date futureDate = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10 * 365)); // 10 years ahead


        certificateDTO.setIssueDate(pastDate);
        certificateDTO.setExpiryDate(futureDate);

        assertEquals(pastDate, certificateDTO.getIssueDate());
        assertEquals(futureDate, certificateDTO.getExpiryDate());
    }

    @Test
    void testExpiryBeforeIssueDate() {
        Date issueDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24); // 1 day before

        certificateDTO.setIssueDate(issueDate);
        certificateDTO.setExpiryDate(expiryDate);

        assertTrue(certificateDTO.getExpiryDate().before(certificateDTO.getIssueDate()));
    }

    @Test
    void testUUIDHandling() {
        UUID uuid1 = UUID.randomUUID();

        certificateDTO.setFreelancerId(uuid1);
        assertEquals(uuid1, certificateDTO.getFreelancerId());
    }

    @Test
    void testSpecialCharacters() {
        String specialString = "!@#$%^&*()_+";
        certificateDTO.setCertificateName(specialString);
        certificateDTO.setIssuedBy(specialString);
        certificateDTO.setCredentialUrl(specialString);

        assertEquals(specialString, certificateDTO.getCertificateName());
        assertEquals(specialString, certificateDTO.getIssuedBy());
        assertEquals(specialString, certificateDTO.getCredentialUrl());
    }
}
