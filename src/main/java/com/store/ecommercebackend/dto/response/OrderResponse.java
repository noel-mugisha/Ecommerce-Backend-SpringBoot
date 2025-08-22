package com.store.ecommercebackend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
}
