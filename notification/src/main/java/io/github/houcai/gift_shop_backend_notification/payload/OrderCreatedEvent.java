package io.github.houcai.gift_shop_backend_notification.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private UUID orderId;
    private String userId;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
