package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.payment.WebhookRequest;
import com.store.ecommercebackend.repositories.OrderRepository;
import com.store.ecommercebackend.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    // Checking out a cart && creating an order
    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var checkoutResponse = checkoutService.checkout(request);
        var uri = uriBuilder.path("/api/v1/orders/{id}").buildAndExpand(checkoutResponse.getOrderId()).toUri();
        return ResponseEntity.created(uri).body(checkoutResponse);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }
}

