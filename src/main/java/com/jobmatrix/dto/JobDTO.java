package com.jobmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Date startDate;
    private Date endDate;
    private String jobResponsibilities;
    private UUID freelancerId;
}
