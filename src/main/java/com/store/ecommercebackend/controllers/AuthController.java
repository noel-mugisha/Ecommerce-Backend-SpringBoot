package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.config.JwtConfig;
import com.store.ecommercebackend.dto.request.LoginUserRequest;
import com.store.ecommercebackend.dto.request.RegisterUserRequest;
import com.store.ecommercebackend.dto.response.AuthResponse;
import com.store.ecommercebackend.dto.response.UserDto;
import com.store.ecommercebackend.exceptions.DuplicateEmailException;
import com.store.ecommercebackend.mappers.UserMapper;
import com.store.ecommercebackend.repositories.UserRepository;
import com.store.ecommercebackend.services.AuthService;
import com.store.ecommercebackend.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtConfig jwtConfig;

    // Authenticate a user
    @PostMapping("/users/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(
            @Valid @RequestBody LoginUserRequest request,
            HttpServletResponse response
    ) {
        // contains the access token
        var authResponse = authService.authenticate(request);
        // generating refreshToken and its cookie
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = generateCookie(refreshToken);
        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    // Register a user
    @PostMapping("/users/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder,
            HttpServletResponse response
    ) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateEmailException("Email is already registered!..");

        var savedUser = authService.register(request);
        var uri = uriBuilder.path("/api/v1/auth/users/{id}").buildAndExpand(savedUser.getId())
                .toUri();
        // accessToken
        var accessToken = jwtService.generateAccessToken(savedUser);
        // refreshToken and its cookie
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        var cookie = generateCookie(refreshToken);
        response.addCookie(cookie);

        return ResponseEntity.created(uri).body(new AuthResponse(accessToken));
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


    // helper method for generating a cookie
    private Cookie generateCookie (String refreshToken) {
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        return cookie;
    }
}
