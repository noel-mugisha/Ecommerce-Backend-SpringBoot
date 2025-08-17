package com.store.ecommercebackend.dto.request;

import com.store.ecommercebackend.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotBlank(message = "Please provide your email")
    @Email(message = "Please provide a valid email")
    @LowerCase(message = "Email must be in lowercase")
    private String email;
    @NotBlank(message = "Please provide your password")
    private String password;
}
