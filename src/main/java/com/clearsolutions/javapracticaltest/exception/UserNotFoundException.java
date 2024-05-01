package com.clearsolutions.javapracticaltest.exception;

public class UserNotFoundException extends RuntimeException {

  private static final String MESSAGE_ID_NOT_FOUND = "User with id %d not found.";

  public UserNotFoundException(long id) {
    super(String.format(MESSAGE_ID_NOT_FOUND, id));
  }
}
