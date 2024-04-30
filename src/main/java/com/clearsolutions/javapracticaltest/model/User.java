package com.clearsolutions.javapracticaltest.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Model class representing a model for storing user information.
 */
@Builder
@Getter
@Setter
public class User {

  private long id;
  private String email;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private String address;
  private String phoneNumber;

}