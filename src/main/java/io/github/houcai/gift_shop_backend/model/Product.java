package io.github.houcai.gift_shop_backend.model;

import io.github.houcai.gift_shop_backend.dto.ProductRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity(name="products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
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
