package com.jobmatrix.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientDTO {

    @NotNull(message = "client_id cannot be null.")
    private UUID clientId;

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
