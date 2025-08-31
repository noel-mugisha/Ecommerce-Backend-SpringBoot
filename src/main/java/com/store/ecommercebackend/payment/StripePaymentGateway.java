package com.store.ecommercebackend.payment;

import com.store.ecommercebackend.entities.Order;
import com.store.ecommercebackend.entities.OrderItem;
import com.store.ecommercebackend.enums.OrderStatus;
import com.store.ecommercebackend.exceptions.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;


    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/checkout-success?orderId=" + order.getId())
                .setCancelUrl("http://localhost:3000/checkout-cancel")
                .putMetadata("order_id", order.getId().toString());

        order.getOrderItems().forEach(item -> {
            var lineItem = createLineItem(item);
            builder.addLineItem(lineItem);
        });

        try {
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException("Error creating checkout session, try again later...");
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var signature = request.getHeaders().get("stripe-signature");
            var event = Webhook.constructEvent(request.getPayload(), signature, webhookSecretKey);
            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var orderId = extractOrderId(event);
                    return Optional.of(new PaymentResult(orderId, OrderStatus.PAID));
                }
                case "payment_intent.payment_failed" -> {
                    var orderId = extractOrderId(event);
                    return Optional.of(new PaymentResult(orderId, OrderStatus.FAILED));
                }
                default -> {
                    return Optional.empty();
                }
            }
        } catch (SignatureVerificationException e) {
            System.err.println("⚠️ Invalid Stripe Webhook headers: " + e.getMessage());
            throw new PaymentException("Invalid signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Could not deserialize Stripe event, Check SDK and API version.")
        );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.getQuantity())
                .setPriceData(createPriceData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                // Stripe expects amounts in cents
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(item))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .setDescription(item.getProduct().getDescription())
                .build();
    }
}
