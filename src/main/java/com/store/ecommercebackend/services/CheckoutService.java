package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.dto.response.CheckoutResponse;
import com.store.ecommercebackend.entities.Order;
import com.store.ecommercebackend.entities.OrderItem;
import com.store.ecommercebackend.enums.OrderStatus;
import com.store.ecommercebackend.exceptions.BadRequestException;
import com.store.ecommercebackend.mappers.OrderMapper;
import com.store.ecommercebackend.repositories.CartRepository;
import com.store.ecommercebackend.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    // Checking out a cart & placing new order
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.findById(request.getCartId()).orElseThrow(() ->
                new BadRequestException("The cart provided doesn't exist!..")
        );
        if (cart.getCartItems().isEmpty())
            throw new BadRequestException("Cart is empty!..");
        var user = authService.getCurrentUser();
        var order = new Order();
        order.setCustomer(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        cart.getCartItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        });
        var savedOrder = orderRepository.save(order);
        return orderMapper.toCheckoutDto(savedOrder);
    }
}
