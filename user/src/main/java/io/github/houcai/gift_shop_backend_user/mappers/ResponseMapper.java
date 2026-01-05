package io.github.houcai.gift_shop_backend_user.mappers;

import io.github.houcai.gift_shop_backend_user.dtos.AddressDTO;
import io.github.houcai.gift_shop_backend_user.dtos.UserResponse;
import io.github.houcai.gift_shop_backend_user.models.Address;
import io.github.houcai.gift_shop_backend_user.models.User;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class ResponseMapper {

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
