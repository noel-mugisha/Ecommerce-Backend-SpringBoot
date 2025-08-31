package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.dto.response.CheckoutResponse;
import com.store.ecommercebackend.entities.Order;
import com.store.ecommercebackend.entities.OrderItem;
import com.store.ecommercebackend.enums.OrderStatus;
import com.store.ecommercebackend.exceptions.BadRequestException;
import com.store.ecommercebackend.payment.PaymentGateway;
import com.store.ecommercebackend.payment.WebhookRequest;
import com.store.ecommercebackend.repositories.CartRepository;
import com.store.ecommercebackend.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    // Checking out a cart & placing new order
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.findById(request.getCartId()).orElseThrow(() ->
                new BadRequestException("The cart provided doesn't exist!..")
        );
        if (cart.getCartItems().isEmpty())
            throw new BadRequestException("Cart is empty!..");

        var user = authService.getCurrentUser();

        // Create new order
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

        // Generate checkout session
        var checkoutSession = paymentGateway.createCheckoutSession(savedOrder);

        // Clear cart after creating checkout session
        cart.clearCartItems();
        cartRepository.save(cart);

        return new CheckoutResponse(savedOrder.getId(), checkoutSession.getCheckoutUrl());
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setOrderStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
