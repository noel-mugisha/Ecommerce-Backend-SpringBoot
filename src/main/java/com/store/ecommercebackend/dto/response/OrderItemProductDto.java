package com.store.ecommercebackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
