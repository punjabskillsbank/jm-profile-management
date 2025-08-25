package com.jobmatrix.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAuditDTO {
    private String phoneNumber;
    private String bio;
    private String profilePhotoURL;
    private String companyName;
    private String companySize;
    private String industry;
    private String timeZone;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String address;
}
