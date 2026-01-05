package io.github.houcai.gift_shop_backend_user.models;

import io.github.houcai.gift_shop_backend_user.dtos.AddressDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;


    public void updateFrom(AddressDTO addressDTO){
        this.street = addressDTO.getStreet();
        this.city = addressDTO.getCity();
        this.state = addressDTO.getState();
        this.country = addressDTO.getCountry();
        this.zipcode = addressDTO.getZipcode();
    }
}
