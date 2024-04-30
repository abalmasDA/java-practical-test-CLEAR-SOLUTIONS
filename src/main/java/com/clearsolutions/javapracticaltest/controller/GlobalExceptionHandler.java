package com.clearsolutions.javapracticaltest.controller;

import com.clearsolutions.javapracticaltest.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
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

  private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);


  /**
   * Global exception handler method to handle exceptions of type {@link Exception}.
   *
   * @param ex The exception being handled.
   * @return A {@link String} with an appropriate error message.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleExceptionErrors(Exception ex) {
    log.error("Handling Exception: {}", ex.getMessage(), ex);
    return "Oops! Something went wrong:( We're working to fix it! Please try again later:";
  }

  /**
   * Handles NoResourceFoundException by returning an HTTP status 404.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoResourceFoundException.class)
  public String handleNoResourceFoundException(NoResourceFoundException ex) {
    log.error("Handling NoResourceFoundException: {}", ex.getMessage(), ex);
    return ex.getMessage();
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
  public Map<String, String> handleArgumentErrors(MethodArgumentNotValidException ex) {
    log.error("Handling MethodArgumentNotValidException: {}", ex.getMessage(), ex);
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach((error -> errors.put(error.getField(), error.getDefaultMessage())));
    return errors;
  }

  /**
   * Handles exceptions that extend ResourceNotFoundException by returning an HTTP status 400.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleNoFoundExceptions(ResourceNotFoundException ex) {
    log.error("Handling ResourceNotFoundException: {}", ex.getMessage(), ex);
    return ex.getMessage();
  }

  /**
   * Handles exceptions IllegalArgumentException by returning an HTTP status 400.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("Handling IllegalArgumentException: {}", ex.getMessage(), ex);
    return ex.getMessage();
  }

  /**
   * Handles exceptions ConstraintViolationException by returning an HTTP status 400.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public String handleIlConstraintViolationException(ConstraintViolationException ex) {
    log.error("Handling ConstraintViolationException: {}", ex.getMessage(), ex);
    return ex.getMessage();
  }

}