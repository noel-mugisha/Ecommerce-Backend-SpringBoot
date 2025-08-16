package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.AddToCartRequest;
import com.store.ecommercebackend.dto.response.CartDto;
import com.store.ecommercebackend.entities.Cart;
import com.store.ecommercebackend.mappers.CartMapper;
import com.store.ecommercebackend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartMapper cartMapper;
    private final CartService cartService;

    // Creating a cart
    @PostMapping
    public ResponseEntity<CartDto> addCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        var cartDto = cartMapper.toDto(cartService.saveCart(cart));
        var uri = uriBuilder.path("/api/v1/carts/{id}")
                .buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    // Adding product to cart
    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddToCartRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var cartItemDto = cartService.addItemToCart(cartId, request);
        var uri = uriBuilder.path("/api/v1/carts/{id}").buildAndExpand(cartItemDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(cartItemDto);
    }

    // Fetching a single cart
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getSingleCart (@PathVariable UUID cartId) {
        var cartDto = cartService.getCartById(cartId);
        return ResponseEntity.ok(cartDto);
    }
}
