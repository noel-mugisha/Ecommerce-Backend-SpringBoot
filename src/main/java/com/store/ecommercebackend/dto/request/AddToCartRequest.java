package com.store.ecommercebackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotNull(message = "Product id must not be empty")
    private Long productId;
}
