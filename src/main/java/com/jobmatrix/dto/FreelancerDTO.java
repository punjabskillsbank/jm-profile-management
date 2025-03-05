package com.jobmatrix.dto;

import com.common.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerDTO {
    private UUID userID;
    private String title;
    private String bio;
    private Double hourlyRate;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;

    @JsonProperty("isAbcMember")
    private boolean isAbcMember;
    private String profilePhotoURL;
    private List<EducationDTO> educations;
    private List<JobDTO> jobs;
    private List<CertificateDTO> certificates;
    private ProfileStatus profileStatus;
}