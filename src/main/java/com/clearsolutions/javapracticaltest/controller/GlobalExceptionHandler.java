package com.clearsolutions.javapracticaltest.controller;

import com.clearsolutions.javapracticaltest.exception.AgeValidationException;
import com.clearsolutions.javapracticaltest.exception.DateRangeException;
import com.clearsolutions.javapracticaltest.exception.ErrorResponse;
import com.clearsolutions.javapracticaltest.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler class for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


  /**
   * Global exception handler method to handle exceptions of type {@link Exception}.
   *
   * @param ex The exception being handled.
   * @return A {@link String} with an appropriate error message.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleExceptionErrors(Exception ex) {
    logger.error("Handling Exception: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(
        "Oops! Something went wrong:( We're working to fix it! Please try again later:)");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }

  /**
   * Handles NoResourceFoundException by returning an HTTP status 404.
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
    logger.error("Handling NoResourceFoundException: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * Handles MethodArgumentNotValidException by returning a map of field errors with their
   * corresponding error messages.
   *
   * @param ex The MethodArgumentNotValidException to be handled.
   * @return A map containing field names as keys and corresponding error messages as values.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleMethodArgumentNotValidExceptions(
      MethodArgumentNotValidException ex) {
    logger.error("Handling MethodArgumentNotValidException: {}", ex.getMessage(), ex);
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach((error -> errors.put(error.getField(), error.getDefaultMessage())));
    return errors;
  }

  /**
   * Handles exceptions that extend UserNotFoundException by returning an HTTP status 400.
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundExceptions(UserNotFoundException ex) {
    logger.error("Handling UserNotFoundException: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles exceptions AgeValidationException by returning an HTTP status 400.
   */
  @ExceptionHandler(AgeValidationException.class)
  public ResponseEntity<?> handleAgeValidationExceptions(AgeValidationException ex) {
    logger.error("Handling AgeValidationException: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles exceptions DateRangeException by returning an HTTP status 400.
   */
  @ExceptionHandler(DateRangeException.class)
  public ResponseEntity<?> handleDateRangeExceptions(DateRangeException ex) {
    logger.error("Handling DateRangeException: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles exceptions ConstraintViolationException by returning an HTTP status 400.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleIlConstraintViolationExceptions(ConstraintViolationException ex) {
    logger.error("Handling ConstraintViolationException: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

}