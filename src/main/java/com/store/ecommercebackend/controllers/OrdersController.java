package com.store.ecommercebackend.controllers;

import com.store.ecommercebackend.dto.response.OrderResponse;
import com.store.ecommercebackend.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    // Getting all orders for a user
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        var orderResponse = ordersService.getAllOrders();
        return ResponseEntity.ok(orderResponse);
    }

    // Getting a single order
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getSingleOrder(
            @PathVariable Long orderId
    ) {
        var orderResponse = ordersService.getSingleOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }
}
