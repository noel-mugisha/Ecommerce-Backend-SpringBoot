package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.ChangePasswordRequest;
import com.store.ecommercebackend.dto.request.UpdateUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.exceptions.UserNotFoundException;
import com.store.ecommercebackend.mappers.UserMapper;
import com.store.ecommercebackend.repositories.UserRepository;
import com.store.ecommercebackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Getting all Users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Getting a single user
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long id) {
        var user = userService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!.."));
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    // Updating user resources
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserResources(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        var existingUser = userService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!.."));
        var updatedUserDto = userService.updateUser(request, existingUser);

        return ResponseEntity.ok(updatedUserDto);
    }

    // Deleting a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Updating user-password
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changeUserPassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!.."));
        if (!user.getPassword().equals(request.getOldPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        else {
            user.setPassword(request.getNewPassword());
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        }
    }

}
