package io.github.houcai.gift_shop_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
