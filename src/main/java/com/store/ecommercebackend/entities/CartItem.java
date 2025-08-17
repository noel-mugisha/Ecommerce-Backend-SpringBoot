package com.store.ecommercebackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    public BigDecimal getTotalPrice () {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

}