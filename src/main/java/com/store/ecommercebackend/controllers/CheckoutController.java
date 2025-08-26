package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.dto.response.ApiErrorResponse;
import com.store.ecommercebackend.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    // Checking out a cart && creating an order
    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            var checkoutResponse = checkoutService.checkout(request);
            var uri = uriBuilder.path("/api/v1/orders/{id}").buildAndExpand(checkoutResponse.getOrderId()).toUri();
            return ResponseEntity.created(uri).body(checkoutResponse);
        } catch (StripeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiErrorResponse.builder()
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .errorReason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                            .message("Error creating the checkout session")
                            .build()
            );
        }
    }

}
