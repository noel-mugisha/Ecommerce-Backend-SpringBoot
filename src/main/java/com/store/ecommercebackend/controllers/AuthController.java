package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.LoginUserRequest;
import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.exceptions.DuplicateEmailException;
import com.store.ecommercebackend.repositories.UserRepository;
import com.store.ecommercebackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    // Authenticate a user
    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticateUser (
            @Valid @RequestBody LoginUserRequest request
    ) {
        authService.authenticate(request);
        return ResponseEntity.ok().build();
    }

    // Register a user
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateEmailException("Email is already registered!..");

        var userDto = authService.register(request);
        var uri = uriBuilder.path("/api/v1/auth/users/{id}").buildAndExpand(userDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
}
