package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.UserDto;
import com.store.ecommercebackend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
