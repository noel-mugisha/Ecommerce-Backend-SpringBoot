package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.ChangePasswordRequest;
import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.request.UpdateUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.entities.User;
import com.store.ecommercebackend.mappers.UserMapper;
import com.store.ecommercebackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Registering a user
    public UserDto createUser(RegisterUserRequest user) {
        var savedUser = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDto(savedUser);
    }

    // Updating user resources
    public UserDto updateUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userMapper.updateUser(request, user);
            userRepository.save(user);
            return userMapper.toDto(user);
        }
        return null;
    }

    // Deleting a user
    public boolean deleteUser (Long id) {
        var user = userRepository.findById(id)
                .orElse(null);
        if (user == null)
            return false;
        userRepository.delete(user);
        return true;
    }

}
