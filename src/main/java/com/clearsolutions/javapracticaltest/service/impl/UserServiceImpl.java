package com.clearsolutions.javapracticaltest.service.impl;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.exception.UserNotFoundException;
import com.clearsolutions.javapracticaltest.mapper.DataMapper;
import com.clearsolutions.javapracticaltest.model.User;
import com.clearsolutions.javapracticaltest.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class for performing operations related to {@link User} models.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final DataMapper<UserDto, User> userMapper;

  private final ObjectMapper objectMapper;

  private final Validator validator;

  @Value("${user.minValidAge}")
  private int minValidAge;

  private long nextId = 1L;

  private final List<User> users;

  /**
   * Creates a new user.
   *
   * @param userDto the user data transfer object containing user information
   * @return the newly created user as a data transfer object
   */
  public UserDto create(UserDto userDto) {
    validateUserAge(userDto.getBirthDate());
    User user = userMapper.toEntity(userDto);
    user.setId(nextId++);
    users.add(user);
    return userMapper.toDto(user);
  }

  /**
   * Updates a User partially based on the provided ID and patch data.
   *
   * @param id        the ID of the user to be updated
   * @param patchData the JSON patch data for partial update
   * @return the updated UserDto
   */
  public UserDto updatePartially(long id, JsonPatch patchData) {
    User user = findById(id);
    UserDto userDto = userMapper.toDto(user);
    UserDto userDtoPatched = applyPatchToDto(patchData, userDto, UserDto.class);
    userMapper.updateEntity(userDtoPatched, user);
    return userMapper.toDto(user);
  }

  /**
   * Updates a user with the given ID using the provided UserDto.
   *
   * @param id      the ID of the user to be updated
   * @param userDto the UserDto containing the updated user information
   * @return the updated UserDto
   */
  public UserDto update(long id, UserDto userDto) {
    User user = findById(id);
    userMapper.updateEntity(userDto, user);
    return userMapper.toDto(user);
  }

  /**
   * Deletes a user with the given id.
   *
   * @param id the id of the user to delete
   */
  public void delete(long id) {
    users.remove(findById(id));
  }

  /**
   * A method to search users based on birthdate range.
   *
   * @param fromDate the start date of the range to search
   * @param toDate   the end date of the range to search
   * @return a list of UserDto objects that fall within the specified birthdate range
   */
  public List<UserDto> searchUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
    validateDateRange(fromDate, toDate);
    return users.stream()
        .filter(
            user -> !user.getBirthDate().isBefore(fromDate) && !user.getBirthDate().isAfter(toDate))
        .map(userMapper::toDto)
        .toList();
  }

  /**
   * Validates the user's age based on the minimum valid age.
   *
   * @param birthDate The birthdate of the user to validate.
   * @throws IllegalArgumentException if the user's age is less than or equal to the minimum valid
   */
  private void validateUserAge(LocalDate birthDate) {
    Period period = Period.between(birthDate, LocalDate.now());
    if (period.getYears() <= minValidAge) {
      throw new IllegalArgumentException("Age must be greater than " + minValidAge);
    }
  }

  /**
   * Validates if the fromDate is before the toDate.
   *
   * @param fromDate the starting date
   * @param toDate   the ending date
   * @throws IllegalArgumentException if fromDate is after toDate
   */
  private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("From date must be before To date");
    }
  }

  /**
   * Find a user by their ID.
   *
   * @param id The ID of the user to find.
   * @return The user with the specified ID.
   * @throws UserNotFoundException If the user with the given ID is not found.
   */
  private User findById(Long id) {
    return users.stream()
        .filter(user -> id.equals(user.getId()))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  /**
   * Applies a JSON patch to a target DTO object and validates the patched object.
   *
   * @param patchData  The JSON patch data to apply
   * @param targetBean The target DTO object to apply the patch to
   * @param classBean  The class of the target DTO object
   * @param <T>        The type of the target DTO object
   * @return The patched and validated DTO object
   * @throws ConstraintViolationException If the patched bean violates any constraints.
   */
  @SneakyThrows
  private <T> T applyPatchToDto(JsonPatch patchData, T targetBean, Class<T> classBean) {
    JsonNode target = objectMapper.convertValue(targetBean, JsonNode.class);
    JsonNode patched = patchData.apply(target);
    T beanPatched = objectMapper.convertValue(patched, classBean);
    Set<ConstraintViolation<T>> violations = validator.validate(beanPatched);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException("Validation failed for patched part", violations);
    }
    return beanPatched;
  }

}