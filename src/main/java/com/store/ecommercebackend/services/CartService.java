package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.AddToCartRequest;
import com.store.ecommercebackend.dto.response.CartItemDto;
import com.store.ecommercebackend.entities.Cart;
import com.store.ecommercebackend.entities.CartItem;
import com.store.ecommercebackend.exceptions.BadRequestException;
import com.store.ecommercebackend.exceptions.ResourceNotFoundException;
import com.store.ecommercebackend.mappers.CartMapper;
import com.store.ecommercebackend.repositories.CartRepository;
import com.store.ecommercebackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    // Adding a cart in the DB
    public Cart saveCart (Cart cart) {
        return cartRepository.save(cart);
    }

    // Adding a cart item to the cart
    public CartItemDto addItemToCart(UUID cartId, AddToCartRequest request) {
        var cart = cartRepository.findById(cartId).orElseThrow(() ->
                new ResourceNotFoundException("Cart doesn't exist...")
        );
        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BadRequestException("The product you are trying to add doesn't exist"));

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        if (cartItem != null)
            cartItem.setQuantity(cartItem.getQuantity()+1);
        else {
            var newCartItem = CartItem.builder()
                    .quantity(1)
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(newCartItem);
        }

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }
}
