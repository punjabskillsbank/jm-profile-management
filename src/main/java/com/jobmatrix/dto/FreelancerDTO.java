package com.jobmatrix.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import com.common.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b

import java.security.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
<<<<<<< HEAD
@AllArgsConstructor
public class FreelancerDTO {
    private UUID freelancerId;
=======
@Builder(toBuilder = true)
public class FreelancerDTO {
    @NotNull(message = "freelancerId cannot be null.")
    private UUID freelancerId;

    @NotBlank(message = "title cannot be null.")
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
    private String title;
    private String bio;
<<<<<<< HEAD
=======

    @NotNull(message = "hourlyRate cannot be null.")
    @Positive(message = "hourlyRate must be greater than 0.")
>>>>>>> 4081ec6b1190e2aed7942a0e690182794161589b
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
