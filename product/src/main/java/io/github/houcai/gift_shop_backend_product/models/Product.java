package io.github.houcai.gift_shop_backend_product.models;

import io.github.houcai.gift_shop_backend_product.dtos.ProductRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity(name="products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // hibernate generated UUID
    private UUID id;

    private String name;

    @Column(length = 1000) // VARCHAR(1000)
    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private String category;

    private String imageUrl;

    private Boolean active = true;

    public void updateFrom(ProductRequest productRequest) {
        this.name = productRequest.getName();
        this.description = productRequest.getDescription();
        this.price = productRequest.getPrice();
        this.stockQuantity = productRequest.getStockQuantity();
        this.category = productRequest.getCategory();
        this.imageUrl = productRequest.getImageUrl();
    }

}
