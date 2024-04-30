package com.clearsolutions.javapracticaltest.controller;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.service.impl.UserServiceImpl;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling requests related to users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserServiceImpl userService;


  /**
   * Creates a new user.
   *
   * @param userDto The user data to be created.
   * @return The created user.
   */
  @PostMapping
  public UserDto create(@Valid @RequestBody UserDto userDto) {
    return userService.create(userDto);
  }

  /**
   * Updates a user partially based on the provided JSON patch document.
   *
   * @param id            The ID of the user to update.
   * @param patchDocument The JSON patch document containing the updates.
   * @return The updated user DTO.
   */
  @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
  public UserDto updatePartially(@PathVariable long id,
      @RequestBody JsonPatch patchDocument) {
    return userService.updatePartially(id, patchDocument);
  }

  /**
   * Update the user information based on the provided user ID.
   *
   * @param id      The ID of the user to update.
   * @param userDto The updated user information.
   * @return The updated user information.
   */
  @PutMapping("/{id}")
  public UserDto update(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
    return userService.update(id, userDto);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id The ID of the user to delete
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) {
    userService.delete(id);
  }

  /**
   * Retrieves a list of user data transfer objects within a specified birthdate range.
   *
   * @param fromDate The start date of the birthdate range.
   * @param toDate   The end date of the birthdate range.
   * @return A list of UserDto objects within the specified birthdate range.
   */
  @GetMapping(value = "/filter", params = {"fromDate", "toDate"})
  public List<UserDto> searchUsersByBirthDateRange(@RequestParam LocalDate fromDate,
      @RequestParam LocalDate toDate) {
    return userService.searchUsersByBirthDateRange(fromDate, toDate);
  }

}