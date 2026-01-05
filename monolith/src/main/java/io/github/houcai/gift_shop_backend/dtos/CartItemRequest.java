package io.github.houcai.gift_shop_backend.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemRequest {
    private UUID productId;
    private Integer quantity;
}
