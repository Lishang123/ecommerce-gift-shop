package io.github.houcai.gift_shop_backend_order.clients;

import io.github.houcai.gift_shop_backend_order.dtos.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;
import java.util.UUID;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{id}")
    Optional<ProductResponse> fetchProductById(@PathVariable UUID id);
}
