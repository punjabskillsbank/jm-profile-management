package com.jobmatrix.exceptionHandling;

public class ServicesOfferedLimitExceededException extends RuntimeException {
  public ServicesOfferedLimitExceededException() {
    super("Cannot assign more than 10 services.");
  }
}
