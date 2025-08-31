package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.enums.OrderStatus;
import com.store.ecommercebackend.repositories.OrderRepository;
import com.store.ecommercebackend.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
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
    private final OrderRepository orderRepository;
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

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook (
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ) {
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow();
            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;
                    var orderId = Long.valueOf(paymentIntent.getMetadata().get("order_id"));
                    var order = orderRepository.findById(orderId).orElseThrow();
                    order.setOrderStatus(OrderStatus.PAID);
                    orderRepository.save(order);
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

