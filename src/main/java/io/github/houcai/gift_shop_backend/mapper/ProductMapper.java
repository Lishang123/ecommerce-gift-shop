package io.github.houcai.gift_shop_backend.mapper;

import io.github.houcai.gift_shop_backend.dto.ProductResponse;
import io.github.houcai.gift_shop_backend.model.Product;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
public final class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                product.getActive()
        );
    }

    public static ProductResponse toProduct(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getImageUrl(),
                product.getActive()
        );
    }

}
