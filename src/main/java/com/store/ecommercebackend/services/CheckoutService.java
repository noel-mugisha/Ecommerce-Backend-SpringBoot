package com.store.ecommercebackend.services;

import com.store.ecommercebackend.dto.request.CheckoutRequest;
import com.store.ecommercebackend.dto.response.CheckoutResponse;
import com.store.ecommercebackend.entities.Order;
import com.store.ecommercebackend.entities.OrderItem;
import com.store.ecommercebackend.enums.OrderStatus;
import com.store.ecommercebackend.exceptions.BadRequestException;
import com.store.ecommercebackend.repositories.CartRepository;
import com.store.ecommercebackend.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    // Checking out a cart & placing new order
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
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

        // Creating a Checkout session
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/checkout-success?orderId=" + order.getId())
                .setCancelUrl("http://localhost:3000/checkout-cancel");
        order.getOrderItems().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getProduct().getName())
                                                    .setDescription(item.getProduct().getDescription())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            builder.addLineItem(lineItem);
        });
        var session = Session.create(builder.build());

        cart.clearCartItems();
        cartRepository.save(cart);
        var savedOrder = orderRepository.save(order);
        return new CheckoutResponse(savedOrder.getId(), session.getUrl());

    }
}
