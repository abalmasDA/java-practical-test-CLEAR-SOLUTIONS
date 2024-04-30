package com.clearsolutions.javapracticaltest.service.impl;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.exception.UserNotFoundException;
import com.clearsolutions.javapracticaltest.mapper.DataMapper;
import com.clearsolutions.javapracticaltest.model.User;
import com.clearsolutions.javapracticaltest.service.UserService;
import com.clearsolutions.javapracticaltest.service.ValidationService;
import com.clearsolutions.javapracticaltest.util.PatchUtil;
import com.github.fge.jsonpatch.JsonPatch;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for performing operations related to {@link User} models.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final ValidationService validationService;

  private final DataMapper<UserDto, User> userMapper;

  private final PatchUtil patchUtil;

  private final List<User> users;

  private long nextId = 1L;

  /**
   * Creates a new user.
   *
   * @param userDto the user data transfer object containing user information
   * @return the newly created user as a data transfer object
   */
  public UserDto create(UserDto userDto) {
    validationService.validateUserAge(userDto.getBirthDate());
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
    UserDto userDtoPatched = patchUtil.applyPatch(patchData, userDto, UserDto.class);
    validationService.validateDto(userDtoPatched);
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
    validationService.validateUserDateRange(fromDate, toDate);
    return users.stream()
        .filter(
            user -> !user.getBirthDate().isBefore(fromDate) && !user.getBirthDate().isAfter(toDate))
        .map(userMapper::toDto)
        .toList();
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

}