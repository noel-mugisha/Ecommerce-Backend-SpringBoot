package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.request.UpdateUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest userRequest);

    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
