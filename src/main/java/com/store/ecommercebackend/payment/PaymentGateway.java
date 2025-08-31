package com.store.ecommercebackend.payment;

import com.store.ecommercebackend.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
