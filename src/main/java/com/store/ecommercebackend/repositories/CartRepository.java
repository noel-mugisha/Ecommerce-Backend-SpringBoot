package com.store.ecommercebackend.repositories;

import com.store.ecommercebackend.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}