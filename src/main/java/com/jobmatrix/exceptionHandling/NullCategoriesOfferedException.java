package com.jobmatrix.exceptionHandling;

public class NullCategoriesOfferedException extends RuntimeException {
    public NullCategoriesOfferedException() {
        super("Categories cannot be null.");
    }
}