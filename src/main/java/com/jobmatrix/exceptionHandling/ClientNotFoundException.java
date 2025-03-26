package com.jobmatrix.exceptionHandling;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException{
    public ClientNotFoundException(UUID clientId) {
        super("Client not found at given clientId: " + clientId);
    }
}
