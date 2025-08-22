package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.response.OrderResponse;
import com.store.ecommercebackend.exceptions.ResourceNotFoundException;
import com.store.ecommercebackend.mappers.OrderMapper;
import com.store.ecommercebackend.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    // Getting all orders
    public List<OrderResponse> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findByCustomer(user);
        return orders.stream().map(orderMapper::toOrderDto).toList();
    }

    public OrderResponse getSingleOrder(Long orderId) {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findByCustomer(user);
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst().map(orderMapper::toOrderDto)
                .orElseThrow(() -> new ResourceNotFoundException("The above order wasn't found!..."));
    }
}


