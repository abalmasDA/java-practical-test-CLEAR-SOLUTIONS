package com.clearsolutions.javapracticaltest.service;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.github.fge.jsonpatch.JsonPatch;
import java.time.LocalDate;
import java.util.List;

public interface UserService {

  UserDto create(UserDto userDto);

  UserDto updatePartially(long id, JsonPatch patchData);

  UserDto update(long id, UserDto userDto);

  void delete(long id);

  List<UserDto> searchUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);

}