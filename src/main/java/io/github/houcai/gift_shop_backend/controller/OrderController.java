package io.github.houcai.gift_shop_backend.controller;

import io.github.houcai.gift_shop_backend.dto.OrderResponse;
import io.github.houcai.gift_shop_backend.service.OrderService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("User-ID") String userId){
        return orderService.createOrder(userId).map(
                orderResponse -> {
                    return new ResponseEntity(orderResponse, HttpStatus.CREATED);
                }
        ).orElseGet(() -> ResponseEntity.badRequest().build());
    }


}
