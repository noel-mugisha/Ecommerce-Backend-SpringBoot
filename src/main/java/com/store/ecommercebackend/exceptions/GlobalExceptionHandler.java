package com.store.ecommercebackend.exceptions;

import com.store.ecommercebackend.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> getErrorResponse(
            HttpStatus status, RuntimeException exception
    ) {
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(status.value())
                .errorReason(status.getReasonPhrase())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationErrors(
            MethodArgumentNotValidException exception
    ) {
        var status = HttpStatus.BAD_REQUEST;
        Map<String, String> errorMessages = new HashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors())
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(status.value())
                .errorReason(status.getReasonPhrase())
                .message(errorMessages)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> userNotFound(
            UserNotFoundException exception
    ) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> productNotFound(
            ProductNotFoundException exception
    ) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> categoryNotFound(
            CategoryNotFoundException exception
    ) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> duplicateEmail(
            DuplicateEmailException exception
    ) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> resourceNotFound(
            ResourceNotFoundException exception
    ) {
        return getErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> badRequest(BadRequestException exception) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> usernameNotFound(UsernameNotFoundException exception) {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> badCredentials(BadCredentialsException exception) {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentException (PaymentException exception) {
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableData () {
        var status = HttpStatus.BAD_REQUEST;
        var errorResponse = ApiErrorResponse.builder()
                .statusCode(status.value())
                .errorReason(status.getReasonPhrase())
                .message("Invalid request Body!...")
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

}
