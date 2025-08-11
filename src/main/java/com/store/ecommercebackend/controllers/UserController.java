package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Getting all Users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Getting a single user
    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleUser(@PathVariable Long id) {
        var user = userService.findUserById(id);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User with id " + id + " doesn't exist..");
    }

    // Registering a user
    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var userDto = userService.createUser(request);
        var uri = uriBuilder.path("/api/v1/users/{id}").buildAndExpand(userDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

}
