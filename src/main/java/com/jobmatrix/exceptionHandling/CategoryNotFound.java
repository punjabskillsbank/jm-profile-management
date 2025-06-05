package com.jobmatrix.exceptionHandling;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(Long categoryId) {
        super("Category not found with ID: " + categoryId   );
    }
}
