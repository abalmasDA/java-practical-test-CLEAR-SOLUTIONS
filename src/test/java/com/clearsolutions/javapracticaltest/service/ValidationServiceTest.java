package com.clearsolutions.javapracticaltest.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clearsolutions.javapracticaltest.exception.AgeValidationException;
import com.clearsolutions.javapracticaltest.exception.DateRangeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

  @Mock
  private Validator validator;

  @Mock
  private Environment environment;

  @InjectMocks
  private ValidationService validationService;

  @Test
  void validateUserAgeTest() {
    LocalDate birthDate = LocalDate.now().minusYears(25);
    when(environment.getRequiredProperty("user.minValidAge")).thenReturn("18");
    assertDoesNotThrow(() -> validationService.validateUserAge(birthDate));
  }

  @Test
  void validateUserAgeThrowsAgeValidationExceptionTest() {
    LocalDate birthDate = LocalDate.now().minusYears(15);
    when(environment.getRequiredProperty("user.minValidAge")).thenReturn("18");
    assertThrows(AgeValidationException.class, () -> validationService.validateUserAge(birthDate));
  }

  @Test
  void validateUserDateRangeTest() {
    LocalDate fromDate = LocalDate.now().minusDays(10);
    LocalDate toDate = LocalDate.now();
    assertDoesNotThrow(() -> validationService.validateUserDateRange(fromDate, toDate));
  }

  @Test
  void validateUserDateRangeThrowsDateRangeExceptionTest() {
    LocalDate fromDate = LocalDate.now();
    LocalDate toDate = LocalDate.now().minusDays(10);
    assertThrows(
        DateRangeException.class, () -> validationService.validateUserDateRange(fromDate, toDate));
  }

  @Test
  void validateDtoTest() {
    Object dto = new Object();
    when(validator.validate(dto)).thenReturn(Collections.emptySet());
    assertDoesNotThrow(() -> validationService.validateDto(dto));
  }

  @Test
  void validateDtoThrowsConstraintViolationExceptionTest() {
    Object dto = new Object();
    Set<ConstraintViolation<Object>> violations = new HashSet<>();
    violations.add(mock(ConstraintViolation.class));
    when(validator.validate(dto)).thenReturn(violations);
    assertThrows(ConstraintViolationException.class, () -> validationService.validateDto(dto));
  }

}