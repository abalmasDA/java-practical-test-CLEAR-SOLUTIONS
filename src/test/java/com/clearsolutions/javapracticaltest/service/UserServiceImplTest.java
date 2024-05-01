package com.clearsolutions.javapracticaltest.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.exception.AgeValidationException;
import com.clearsolutions.javapracticaltest.mapper.DataMapper;
import com.clearsolutions.javapracticaltest.model.User;
import com.clearsolutions.javapracticaltest.service.impl.UserServiceImpl;
import com.clearsolutions.javapracticaltest.util.PatchUtil;
import com.github.fge.jsonpatch.JsonPatch;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private UserServiceImpl userService;

  @Mock
  private DataMapper<UserDto, User> userMapper;

  @Mock
  private ValidationService validationService;

  @Mock
  private PatchUtil patchUtil;

  private List<User> users = new ArrayList<>();

  private final long USER_ID = 0L;

  private User user;

  private UserDto userDto;

  @BeforeEach
  public void init() {
    userService = new UserServiceImpl(validationService, userMapper, patchUtil, users);
    user = User.builder()
        .id(0L)
        .email("test@example.com")
        .firstName("Test")
        .lastName("Test")
        .birthDate(LocalDate.of(1999, 1, 1))
        .address("Test Address")
        .phoneNumber("Test Phone")
        .build();
    userDto = UserDto.builder()
        .id(0L)
        .email("test@example.com")
        .firstName("Test")
        .lastName("Test")
        .birthDate(LocalDate.of(1999, 1, 1))
        .address("Test Address")
        .phoneNumber("Test Phone")
        .build();
    users.add(user);
  }

  @Test
  void testCreateUserValidAge() {
    doNothing().when(validationService).validateUserAge(any(LocalDate.class));
    when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
    when(userMapper.toDto(any(User.class))).thenReturn(userDto);
    UserDto createdUser = userService.create(userDto);
    assertNotNull(createdUser);
    assertEquals(userDto.getId(), createdUser.getId());
  }

  @Test
  void testCreateUserInvalidAge() {
    doThrow(new AgeValidationException("Invalid age")).when(validationService)
        .validateUserAge(any(LocalDate.class));
    assertThrows(AgeValidationException.class, () -> userService.create(userDto));
  }

  @Test
  void updatePartiallyTest() {
    JsonPatch patchData = new JsonPatch(List.of());
    when(userMapper.toDto(user)).thenReturn(userDto);
    when(patchUtil.applyPatch(patchData, userDto, UserDto.class)).thenReturn(userDto);
    doNothing().when(validationService).validateDto(userDto);
    when(userMapper.toDto(user)).thenReturn(userDto);
    UserDto result = userService.updatePartially(USER_ID, patchData);
    assertEquals(userDto, result);
  }

  @Test
  void updateTest() {
    when(userService.update(anyLong(), any(UserDto.class))).thenReturn(userDto);
    when(userMapper.toDto(user)).thenReturn(userDto);
    UserDto updatedUserDto = userService.update(USER_ID, userDto);
    assertEquals(userDto, updatedUserDto);
  }

  @Test
  void deleteTest() {
    userService.delete(USER_ID);
    assertTrue(users.isEmpty());
  }

  @Test
  void searchUsersByBirthDateRangeTest() {
    LocalDate fromDate = LocalDate.of(1998, 12, 31);
    LocalDate toDate = LocalDate.of(2005, 1, 2);
    List<UserDto> expectedUserDtos = new ArrayList<>();
    expectedUserDtos.add(userDto);
    when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
      User user = invocation.getArgument(0);
      return UserDto.builder()
          .id(user.getId())
          .email(user.getEmail())
          .firstName(user.getFirstName())
          .lastName(user.getLastName())
          .birthDate(user.getBirthDate())
          .address(user.getAddress())
          .phoneNumber(user.getPhoneNumber())
          .build();
    });
    doNothing().when(validationService)
        .validateUserDateRange(any(LocalDate.class), any(LocalDate.class));
    List<UserDto> result = userService.searchUsersByBirthDateRange(fromDate, toDate);
    assertNotNull(result);
    assertEquals(1, result.size());
  }

}