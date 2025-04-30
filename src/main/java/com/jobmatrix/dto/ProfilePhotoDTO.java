package com.jobmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for profile photo URL refresh requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhotoDTO {
    private String photoUrl;
} 