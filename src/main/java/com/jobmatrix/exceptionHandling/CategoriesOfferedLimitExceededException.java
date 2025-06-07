package com.jobmatrix.exceptionHandling;

public class CategoriesOfferedLimitExceededException extends RuntimeException {
    public CategoriesOfferedLimitExceededException() {
        super("Cannot assign more than 10 categories.");
    }
}