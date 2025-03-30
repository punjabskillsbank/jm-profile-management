package com.jobmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerDTO {
    private UUID userId;
    private String title;
    private String bio;
    private Double hourlyRate;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;
    private String profilePhotoURL;
    private List<EducationDTO> educations;
    private List<JobDTO> jobs;
    private List<CertificateDTO> certificates;
    private String profileStatus;
    private Boolean isAbcMember;
} 