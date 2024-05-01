package com.clearsolutions.javapracticaltest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserServiceImpl userService;

  @Autowired
  private ObjectMapper objectMapper;

  private final long USER_ID = 1L;

  private UserDto userDto;


  @BeforeEach
  public void setUp() {
    userDto = UserDto.builder()
        .id(USER_ID)
        .email("test@email.com")
        .firstName("firstName")
        .lastName("lastName")
        .birthDate(LocalDate.parse("2000-01-01"))
        .address("address")
        .phoneNumber("phoneNumber")
        .build();
  }

  @Test
  void createTest() throws Exception {
    String content = objectMapper.writeValueAsString(userDto);
    when(userService.create(any(UserDto.class))).thenReturn(userDto);
    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isOk())
        .andExpect(content().json(content));
  }

  @Test
  void updatePartiallyTest() throws Exception {
    JsonPatch patchDocument = new JsonPatch(List.of());
    when(userService.updatePartially(anyLong(), any(JsonPatch.class))).thenReturn(userDto);
    mockMvc.perform(patch("/users/{id}", USER_ID)
            .contentType("application/json-patch+json")
            .content(patchDocument.toString().getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk());
  }

  @Test
  void updateTest() throws Exception {
    String content = objectMapper.writeValueAsString(userDto);
    when(userService.update(anyLong(), any(UserDto.class))).thenReturn(userDto);
    mockMvc.perform(put("/users/{id}", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isOk())
        .andExpect(content().json(content));
  }

  @Test
  void deleteTest() throws Exception {
    mockMvc.perform(delete("/users/{id}", USER_ID))
        .andExpect(status().isOk());
  }

  @Test
  void searchUsersByBirthDateRangeTest() throws Exception {
    List<UserDto> userDtos = Arrays.asList(userDto);
    when(userService.searchUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
        .thenReturn(userDtos);
    mockMvc.perform(get("/users")
            .param("fromDate", "2000-01-01")
            .param("toDate", "2000-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(userDtos)));
  }

}