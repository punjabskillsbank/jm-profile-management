package com.jobmatrix.exceptionHandling;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(UUID userId) {
        super("User not found at given userId: " + userId);
    }
}
