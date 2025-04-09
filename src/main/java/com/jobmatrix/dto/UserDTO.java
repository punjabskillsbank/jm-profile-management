package com.jobmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String userRole;
    private String accountStatus;
    private String createdAt;
    private String updatedAt;
}
