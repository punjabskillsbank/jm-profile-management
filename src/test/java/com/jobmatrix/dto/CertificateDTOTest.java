package com.jobmatrix.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.UUID;

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
        assertNull(dto.getCertificateId());
        assertNull(dto.getCertificateName());
        assertNull(dto.getIssuedBy());
        assertNull(dto.getIssueDate());
        assertNull(dto.getExpiryDate());
        assertNull(dto.getCredentialUrl());
        assertNull(dto.getUserId());
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

        CertificateDTO dto = new CertificateDTO(certificateId, certificateName, issuedBy, issueDate, expiryDate, credentialUrl, userId);

        assertEquals(certificateId, dto.getCertificateId());
        assertEquals(certificateName, dto.getCertificateName());
        assertEquals(issuedBy, dto.getIssuedBy());
        assertEquals(issueDate, dto.getIssueDate());
        assertEquals(expiryDate, dto.getExpiryDate());
        assertEquals(credentialUrl, dto.getCredentialUrl());
        assertEquals(userId, dto.getUserId());
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

        certificateDTO.setCertificateId(certificateId);
        certificateDTO.setCertificateName(certificateName);
        certificateDTO.setIssuedBy(issuedBy);
        certificateDTO.setIssueDate(issueDate);
        certificateDTO.setExpiryDate(expiryDate);
        certificateDTO.setCredentialUrl(credentialUrl);
        certificateDTO.setUserId(userId);

        assertEquals(certificateId, certificateDTO.getCertificateId());
        assertEquals(certificateName, certificateDTO.getCertificateName());
        assertEquals(issuedBy, certificateDTO.getIssuedBy());
        assertEquals(issueDate, certificateDTO.getIssueDate());
        assertEquals(expiryDate, certificateDTO.getExpiryDate());
        assertEquals(credentialUrl, certificateDTO.getCredentialUrl());
        assertEquals(userId, certificateDTO.getUserId());
    }

    @Test
    void testNullValues() {
        certificateDTO.setCertificateId(null);
        certificateDTO.setCertificateName(null);
        certificateDTO.setIssuedBy(null);
        certificateDTO.setIssueDate(null);
        certificateDTO.setExpiryDate(null);
        certificateDTO.setCredentialUrl(null);
        certificateDTO.setUserId(null);

        assertNull(certificateDTO.getCertificateId());
        assertNull(certificateDTO.getCertificateName());
        assertNull(certificateDTO.getIssuedBy());
        assertNull(certificateDTO.getIssueDate());
        assertNull(certificateDTO.getExpiryDate());
        assertNull(certificateDTO.getCredentialUrl());
        assertNull(certificateDTO.getUserId());
    }

    @Test
    void testBoundaryConditionsForDates() {
        Date pastDate = new Date(System.currentTimeMillis() - 10L * 365 * 24 * 60 * 60 * 1000); // 10 years ago
        Date futureDate = new Date(System.currentTimeMillis() + 10L * 365 * 24 * 60 * 60 * 1000); // 10 years ahead

        certificateDTO.setIssueDate(pastDate);
        certificateDTO.setExpiryDate(futureDate);

        assertEquals(pastDate, certificateDTO.getIssueDate());
        assertEquals(futureDate, certificateDTO.getExpiryDate());
    }

    @Test
    void testSameIssueAndExpiryDate() {
        Date sameDate = new Date(System.currentTimeMillis());

        certificateDTO.setIssueDate(sameDate);
        certificateDTO.setExpiryDate(sameDate);

        assertEquals(sameDate, certificateDTO.getIssueDate());
        assertEquals(sameDate, certificateDTO.getExpiryDate());
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
        UUID uuid2 = UUID.randomUUID();

        certificateDTO.setUserId(uuid1);
        assertEquals(uuid1, certificateDTO.getUserId());

        certificateDTO.setUserId(uuid2);
        assertEquals(uuid2, certificateDTO.getUserId());
    }

    @Test
    void testEmptyStrings() {
        certificateDTO.setCertificateName("");
        certificateDTO.setIssuedBy("");
        certificateDTO.setCredentialUrl("");

        assertEquals("", certificateDTO.getCertificateName());
        assertEquals("", certificateDTO.getIssuedBy());
        assertEquals("", certificateDTO.getCredentialUrl());
    }

    @Test
    void testLongStrings() {
        String longString = "A".repeat(1000);
        certificateDTO.setCertificateName(longString);
        certificateDTO.setIssuedBy(longString);
        certificateDTO.setCredentialUrl(longString);

        assertEquals(longString, certificateDTO.getCertificateName());
        assertEquals(longString, certificateDTO.getIssuedBy());
        assertEquals(longString, certificateDTO.getCredentialUrl());
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
