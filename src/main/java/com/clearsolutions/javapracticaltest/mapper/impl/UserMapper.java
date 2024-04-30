package com.clearsolutions.javapracticaltest.mapper.impl;

import com.clearsolutions.javapracticaltest.dto.UserDto;
import com.clearsolutions.javapracticaltest.mapper.DataMapper;
import com.clearsolutions.javapracticaltest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper implements DataMapper<UserDto, User> {

  @Mapping(target = "id", ignore = true)
  public abstract User toEntity(UserDto userDto);

  @Override
  @Mapping(target = "id", ignore = true)
  public abstract User updateEntity(UserDto userDto, @MappingTarget User user);

}