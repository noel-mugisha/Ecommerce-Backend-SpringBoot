package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.response.CartDto;
import com.store.ecommercebackend.dto.response.CartItemDto;
import com.store.ecommercebackend.entities.Cart;
import com.store.ecommercebackend.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
