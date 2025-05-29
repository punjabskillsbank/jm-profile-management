package com.jobmatrix.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientUpdateRequest {

    @Pattern(regexp = "^(\\+91)?[6789]\\d{9}$", message= "Please enter a valid Phone Number")
    private String phoneNumber;
    @Size(max = 500, message = "bio cannot exceed 500 characters")
    private String bio;
    private String profilePhotoURL;
    @Size(max = 100, message = "Company name must be at most 100 characters")
    private String companyName;
    private String industry;
    @Pattern(regexp = "^([1-9]\\d*(?:-[1-9]\\d*)?)$", message = "Invalid format")
    private String companySize;
    private String timeZone;
    private String country;
    private String state;
    private String city;
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Please enter a valid Indian pincode")
    private String postalCode;
    private String address;

}

