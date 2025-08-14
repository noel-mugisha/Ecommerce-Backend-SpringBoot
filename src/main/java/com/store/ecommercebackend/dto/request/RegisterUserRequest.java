package com.store.ecommercebackend.dto.request;

import com.store.ecommercebackend.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    @NotBlank(message = "Your name is required")
    private String name;

    @NotBlank(message = "Your email is required")
    @Email(message = "Email must be valid")
    @LowerCase(message = "Email must be lowercase")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Your password should be at least 6 characters long")
    private String password;
}
