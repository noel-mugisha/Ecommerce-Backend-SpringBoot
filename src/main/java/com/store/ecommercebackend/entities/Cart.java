package com.store.ecommercebackend.entities;

import com.store.ecommercebackend.exceptions.ProductNotFoundException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "cart")
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        var total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }

    public CartItem getCartItem(Long id) {
        return this.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with id:" + id + " not found!.."));
    }

    public void clearCartItems () {
        cartItems.clear();
    }
}