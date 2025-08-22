package com.store.ecommercebackend.repositories;

import com.store.ecommercebackend.entities.Order;
import com.store.ecommercebackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
}
