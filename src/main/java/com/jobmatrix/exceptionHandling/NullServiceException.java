package com.jobmatrix.exceptionHandling;

public class NullServiceException extends RuntimeException {
    public NullServiceException() {
        super("Service cannot be null.");
    }
}
