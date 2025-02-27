package com.jobmatrix.jm_profile_management.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
// ... other imports

public record FreelancerDTO(
        @JsonProperty("sub") UUID sub,
        @JsonProperty("title") String title,
        @JsonProperty("bio") String bio,
        @JsonProperty("hourly_rate") BigDecimal hourlyRate,
        @JsonProperty("address") String address,
        @JsonProperty("phone_number") String phoneNumber,
        @JsonProperty("is_abc_member") boolean isAbcMember,
        @JsonProperty("profile_photo") String profilePhoto,
        @JsonProperty("education") List<EducationEntity> education,
        @JsonProperty("workExperience") List<WorkExperienceEntity> workExperience,
        @JsonProperty("certificates") List<CertificatesEntity> certificates,
        @JsonProperty("profile_status") ProfileStatus profileStatus
) {}