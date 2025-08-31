package com.store.ecommercebackend.payment;

import com.store.ecommercebackend.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private OrderStatus paymentStatus;
}
