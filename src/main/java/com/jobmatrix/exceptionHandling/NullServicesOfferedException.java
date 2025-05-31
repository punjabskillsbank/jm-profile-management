package com.jobmatrix.exceptionHandling;

public class NullServicesOfferedException extends RuntimeException {
    public NullServicesOfferedException() {
        super("Service cannot be null.");
    }
}
