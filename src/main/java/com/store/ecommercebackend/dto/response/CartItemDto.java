package com.store.ecommercebackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Long id;
    private CartProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
