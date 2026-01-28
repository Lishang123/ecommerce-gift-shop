package io.github.houcai.gift_shop_backend_user.dtos;

import io.github.houcai.gift_shop_backend_user.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
