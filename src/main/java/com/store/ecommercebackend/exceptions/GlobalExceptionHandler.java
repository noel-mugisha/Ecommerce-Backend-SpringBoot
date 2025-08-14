package com.store.ecommercebackend.exceptions;

import com.store.ecommercebackend.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationErrors (
            MethodArgumentNotValidException exception
    ) {
        var error = HttpStatus.BAD_REQUEST;
        Map<String, String> errorMessages = new HashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(error.value())
                .errorReason(error.getReasonPhrase())
                .message(errorMessages)
                .build();
        return new ResponseEntity<>(errorResponse, error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> userNotFound (
            UserNotFoundException exception
    ) {
        var error = HttpStatus.NOT_FOUND;
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(error.value())
                .errorReason(error.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> productNotFound (
            ProductNotFoundException exception
    ) {
        var error = HttpStatus.NOT_FOUND;
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(error.value())
                .errorReason(error.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, error);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> categoryNotFound (
            CategoryNotFoundException exception
    ) {
        var error = HttpStatus.NOT_FOUND;
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(error.value())
                .errorReason(error.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, error);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> duplicateEmail (
            DuplicateEmailException exception
    ) {
        var error = HttpStatus.BAD_REQUEST;
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(error.value())
                .errorReason(error.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, error);
    }

}
