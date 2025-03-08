package com.jobmatrix.dto;

import com.common.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull(message = "userID cannot be null.")
    private UUID userID;

    @NotNull(message = "title cannot be null.")
    private String title;

    @NotNull(message = "bio cannot be null.")
    private String bio;

    @NotNull(message = "hourlyRate cannot be null.")
    private Double hourlyRate;
    private String address;
    private String city;
    private String state;
    private String country;

    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Please enter a valid postal code")
    private String postalCode;

    @Pattern(regexp = "^(\\+91)?[6789]\\d{9}$", message = "Please enter a valid Phone Number")
    private String phoneNumber;

    @JsonProperty("isAbcMember")
    private boolean isAbcMember;
    private String profilePhotoURL;
    private List<EducationDTO> educations;
    private List<JobDTO> jobs;
    private List<CertificateDTO> certificates;

    @NotNull(message = "profileStatus cannot be null.")
    private ProfileStatus profileStatus;
}