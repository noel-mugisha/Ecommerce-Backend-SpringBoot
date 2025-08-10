package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.UserDto;
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
}
