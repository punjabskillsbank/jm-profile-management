package com.jobmatrix.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {

    @NotNull(message = "educationId cannot be null.")
    private Long educationId;

    @NotBlank(message = "degree cannot be blank.")
    @Size(max = 100, message = "degree must be at most 100 characters.")
    private String degree;

    @NotBlank(message = "specialization cannot be blank.")
    @Size(max = 100, message = "specialization must be at most 100 characters.")
    private String specialization;

    @NotBlank(message = "institution cannot be blank.")
    @Size(max = 150, message = "institution name must be at most 150 characters.")
    private String institution;

    @NotNull(message = "startYear cannot be null.")
    @Min(value = 1950, message = "startYear must be after 1950.")
    private Integer startYear;

    @NotNull(message = "endYear cannot be null.")
    @Min(value = 1950, message = "endYear must be after 1950.")
    private Integer endYear;

    @NotNull(message = "freelancerId cannot be null.")
    private UUID freelancerId;

}
