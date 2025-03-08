package com.jobmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {

    private Long educationId;
    private String degree;
    private String specialization;
    private String institution;
    private Integer startYear;
    private Integer endYear;
    private UUID userId;
}
