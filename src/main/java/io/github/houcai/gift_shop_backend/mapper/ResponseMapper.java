package io.github.houcai.gift_shop_backend.mapper;

import io.github.houcai.gift_shop_backend.dto.*;
import io.github.houcai.gift_shop_backend.model.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.stream.Collectors;

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

    public static AddressDTO toAddressDTO(Address address){
        if (address != null){
            return new AddressDTO(
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getCountry(),
                    address.getZipcode()
            );
        }
        else return null;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                toAddressDTO(user.getAddress())
        );
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        if (orderItem != null){
            return new OrderItemDTO(
                    orderItem.getId(),
                    orderItem.getProduct().getId(),
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
