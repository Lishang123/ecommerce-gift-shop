package io.github.houcai.gift_shop_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="cart")
@Data
@NoArgsConstructor
public class CartItem {

    public CartItem(User user, Product product, Integer quantity, BigDecimal price) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @JoinColumn(name="user_id", nullable = false) // user cannot be null.
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false) // product cannot be null.
    private Product product;

    private Integer quantity;
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
