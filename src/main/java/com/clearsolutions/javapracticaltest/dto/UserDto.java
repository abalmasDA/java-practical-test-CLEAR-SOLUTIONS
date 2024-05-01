package com.clearsolutions.javapracticaltest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) representing the user info.
 */
@Builder
@Getter
public class UserDto {

  private long id;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Past
  @NotNull
  private LocalDate birthDate;

  private String address;

  private String phoneNumber;

}