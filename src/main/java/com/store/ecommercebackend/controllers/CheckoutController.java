package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

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

    @PostMapping("/webook")
    public ResponseEntity<Void> handleWebhook (
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ) {
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                }
                case "payment_intent.failed" -> {
                }
            }
            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            System.err.println("⚠️ Invalid Stripe Webhook signature: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

