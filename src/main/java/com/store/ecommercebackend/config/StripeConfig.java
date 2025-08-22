package com.store.ecommercebackend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secret_key")
    private String stripeSecretKey;

    @PostConstruct
    public void init () {

    }
}
