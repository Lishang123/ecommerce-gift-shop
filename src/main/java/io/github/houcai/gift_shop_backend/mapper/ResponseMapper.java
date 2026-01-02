package io.github.houcai.gift_shop_backend.mapper;

import io.github.houcai.gift_shop_backend.dto.AddressDTO;
import io.github.houcai.gift_shop_backend.dto.ProductResponse;
import io.github.houcai.gift_shop_backend.dto.UserResponse;
import io.github.houcai.gift_shop_backend.model.Address;
import io.github.houcai.gift_shop_backend.model.Product;
import io.github.houcai.gift_shop_backend.model.User;
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

}
