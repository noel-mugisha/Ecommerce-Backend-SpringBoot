package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.mappers.UserMapper;
import com.store.ecommercebackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // Get all users
    public List<UserDto> getAllUsers () {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    // Getting a single user
    public UserDto findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public UserDto createUser(RegisterUserRequest user) {
        var savedUser = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDto(savedUser);
    }
}
