package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.LoginUserRequest;
import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.response.AuthResponse;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.exceptions.DuplicateEmailException;
import com.store.ecommercebackend.mappers.UserMapper;
import com.store.ecommercebackend.repositories.UserRepository;
import com.store.ecommercebackend.services.AuthService;
import com.store.ecommercebackend.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    // Authenticate a user
    @PostMapping("/users/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(
            @Valid @RequestBody LoginUserRequest request
    ) {
        var authResponse = authService.authenticate(request);
        System.out.println(authResponse);
        return ResponseEntity.ok(authResponse);
    }

    // Register a user
    @PostMapping("/users/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateEmailException("Email is already registered!..");

        var savedUser = authService.register(request);
        var uri = uriBuilder.path("/api/v1/auth/users/{id}").buildAndExpand(savedUser.getId())
                .toUri();
        var token = jwtService.generateToken(savedUser);
        return ResponseEntity.created(uri).body(new AuthResponse(token));
    }

    // validate a token
    @GetMapping("/validate")
    public void validateToken() {
    }

    // me
    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
