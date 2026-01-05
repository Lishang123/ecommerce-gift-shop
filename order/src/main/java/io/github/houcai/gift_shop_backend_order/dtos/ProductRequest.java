package io.github.houcai.gift_shop_backend_order.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Represents a user request of creating or updating a product.
 */
@Data
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private String imageUrl;
}
