package com.clearsolutions.javapracticaltest.service;

import com.clearsolutions.javapracticaltest.exception.AgeValidationException;
import com.clearsolutions.javapracticaltest.exception.DateRangeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

  private final Validator validator;

  @Value("${user.minValidAge}")
  private int minValidAge;

  /**
   * Validates the user's age based on the minimum valid age.
   *
   * @param birthDate The birthdate of the user to validate.
   * @throws AgeValidationException if the user's age is less than or equal to the minimum valid
   *                                age.
   */
  public void validateUserAge(LocalDate birthDate) {
    Period period = Period.between(birthDate, LocalDate.now());
    if (period.getYears() < minValidAge) {
      throw new AgeValidationException("User must be at least " + minValidAge + " years old.");
    }
  }

  /**
   * Validates if the fromDate is before the toDate.
   *
   * @param fromDate the starting date
   * @param toDate   the ending date
   * @throws DateRangeException if fromDate is after toDate.
   */
  public void validateUserDateRange(LocalDate fromDate, LocalDate toDate) {
    if (fromDate.isAfter(toDate)) {
      throw new DateRangeException("From date must be before To date.");
    }
  }

  /**
   * Validates a DTO object.
   *
   * @param dto The DTO object to validate
   * @param <T> The type of the DTO object
   * @throws ConstraintViolationException If the DTO violates any constraints
   */
  public <T> void validateDto(T dto) {
    Set<ConstraintViolation<T>> violations = validator.validate(dto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException("Validation failed for DTO", violations);
    }
  }

}