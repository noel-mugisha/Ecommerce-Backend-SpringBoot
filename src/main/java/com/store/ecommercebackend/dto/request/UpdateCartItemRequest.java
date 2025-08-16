package com.store.ecommercebackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Product quantity must be provided!...")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;
}
