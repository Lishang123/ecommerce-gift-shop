package io.github.houcai.gift_shop_backend_notification.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private UUID productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
