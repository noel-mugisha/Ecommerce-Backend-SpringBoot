package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.AddToCartRequest;
import com.store.ecommercebackend.dto.request.UpdateCartItemRequest;
import com.store.ecommercebackend.dto.response.CartDto;
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
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Adding a cart item to the cart
    public CartItemDto addItemToCart(UUID cartId, AddToCartRequest request) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart doesn't exist"));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BadRequestException("Product doesn't exist"));

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setQuantity(0); // Will be incremented below
                    newItem.setProduct(product);
                    newItem.setCart(cart);
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        Cart managedCart = cartRepository.save(cart);
        CartItem managedCartItem = managedCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow();

        return cartMapper.toDto(managedCartItem);
    }

    // Find cart by id
    public CartDto getCartById(UUID id) {
        var cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id:" + id + " not found!.."));
        return cartMapper.toDto(cart);
    }

    // Updating cart item
    public CartItemDto updateCartItem(UUID cartId, Long productId, UpdateCartItemRequest request) {
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart with the above id doesn't exist")
        );
        var cartItem = cart.getCartItem(productId);

        cartItem.setQuantity(request.getQuantity());

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    // Removing a cart item
    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart with the above id doesn't exist")
        );
        var cartItem = cart.getCartItem(productId);
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
    }


    public void clearCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart with the above id doesn't exist")
        );
        cart.clearCartItems();
        cartRepository.save(cart);
    }
}
