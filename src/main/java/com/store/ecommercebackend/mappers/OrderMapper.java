package com.store.ecommercebackend.mappers;

import com.store.ecommercebackend.dto.response.CheckoutResponse;
import com.store.ecommercebackend.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    CheckoutResponse toDto(Order order);
}
