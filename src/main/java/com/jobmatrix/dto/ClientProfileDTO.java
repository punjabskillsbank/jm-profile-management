package com.jobmatrix.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientProfileDTO {


    @NotNull(message = "user_id cannot be null. It must be linked to a user.")
    private UUID user_id;

    @Pattern(regexp = "^(\\+91)?[6789]\\d{9}$", message = "Please enter a valid Phone Number")
    private String phoneNumber;

    @Size(max = 500, message = "bio cannot exceed 500 characters")
    private String bio;
    private String profilePhotoURL;

    @NotBlank(message = "company name can't be blank")
    @Size(max = 100, message = "Company name must be at most 100 characters")
    private String companyName;

    @Pattern(regexp = "^([1-9]\\d*(?:-[1-9]\\d*)?)$", message = "Invalid format")
    private String companySize;

    private String industry;

    private String timeZone = "Asia/Kolkata";

    private String city;
    private String state;
    private String country = "India";


    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Please enter a valid postal code")
    private String postalCode;

    private String address;

}

