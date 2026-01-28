package io.github.houcai.gift_shop_backend_order.clients;

import io.github.houcai.gift_shop_backend_order.dtos.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange
public interface UserServiceClient {

    @GetExchange("/api/users/{id}")
    Optional<UserResponse> fetchUserById(@PathVariable Long id);
}


