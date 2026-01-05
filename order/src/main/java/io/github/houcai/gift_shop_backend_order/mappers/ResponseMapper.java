package io.github.houcai.gift_shop_backend_order.mappers;

import io.github.houcai.gift_shop_backend_order.dtos.AddressDTO;
import io.github.houcai.gift_shop_backend_order.dtos.OrderItemDTO;
import io.github.houcai.gift_shop_backend_order.dtos.OrderResponse;
import io.github.houcai.gift_shop_backend_order.dtos.ProductResponse;
import io.github.houcai.gift_shop_backend_order.models.Order;
import io.github.houcai.gift_shop_backend_order.models.OrderItem;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class ResponseMapper {


    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        if (orderItem != null){
            return new OrderItemDTO(
                    orderItem.getId(),
                    orderItem.getProductId(),
                    orderItem.getQuantity(),
                    orderItem.getPrice(),
                    orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
            );
        }
        return null;
    }

    public static OrderResponse toResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(ResponseMapper::toOrderItemDTO)
                        .collect(Collectors.toUnmodifiableList()),
                order.getCreatedAt()
        );
    }

}
