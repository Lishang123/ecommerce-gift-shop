package io.github.houcai.gift_shop_backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemRequest {
    private UUID productId;
    private Integer quantity;
}
