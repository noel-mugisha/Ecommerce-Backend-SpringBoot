package com.store.ecommercebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;
}
