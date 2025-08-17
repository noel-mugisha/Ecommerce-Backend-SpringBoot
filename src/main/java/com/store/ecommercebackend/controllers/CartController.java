package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.request.AddToCartRequest;
import com.store.ecommercebackend.dto.request.UpdateCartItemRequest;
import com.store.ecommercebackend.dto.response.CartDto;
import com.store.ecommercebackend.dto.response.CartItemDto;
import com.store.ecommercebackend.entities.Cart;
import com.store.ecommercebackend.mappers.CartMapper;
import com.store.ecommercebackend.services.CartService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CartDto> getSingleCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCartById(cartId);
        return ResponseEntity.ok(cartDto);
    }

    // Updating cartItem
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, request);
        return ResponseEntity.ok(cartItemDto);
    }

    // Deleting a cart item
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    // Clear the cart, we are not deleting the cart
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
