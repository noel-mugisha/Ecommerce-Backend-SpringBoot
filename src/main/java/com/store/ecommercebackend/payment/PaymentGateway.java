package com.store.ecommercebackend.payment;

import com.store.ecommercebackend.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);
}
