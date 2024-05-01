package com.clearsolutions.javapracticaltest.mapper;

import org.mapstruct.MappingTarget;

public interface DataMapper<D, E> {

  E toEntity(D dto);

  D toDto(E entity);

  E updateEntity(D dto, @MappingTarget E entity);

}