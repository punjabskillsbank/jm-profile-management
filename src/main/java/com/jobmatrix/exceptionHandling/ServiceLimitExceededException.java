package com.jobmatrix.exceptionHandling;

public class ServiceLimitExceededException extends RuntimeException {
    public ServiceLimitExceededException() {
        super("Cannot assign more than 10 services.");
    }
}
