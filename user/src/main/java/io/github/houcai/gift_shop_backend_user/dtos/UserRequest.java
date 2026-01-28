package io.github.houcai.gift_shop_backend_user.dtos;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
}
