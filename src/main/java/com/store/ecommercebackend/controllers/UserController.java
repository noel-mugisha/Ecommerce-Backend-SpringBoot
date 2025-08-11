package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.UserDto;
import com.store.ecommercebackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
