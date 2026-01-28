package io.github.houcai.gift_shop_backend_product.mappers;

import io.github.houcai.gift_shop_backend_product.dtos.ProductResponse;
import io.github.houcai.gift_shop_backend_product.models.Product;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ResponseMapper {

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

}
