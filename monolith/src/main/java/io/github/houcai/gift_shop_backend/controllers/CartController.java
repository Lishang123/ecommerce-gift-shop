package io.github.houcai.gift_shop_backend.controllers;

import io.github.houcai.gift_shop_backend.dtos.CartItemRequest;
import io.github.houcai.gift_shop_backend.models.CartItem;
import io.github.houcai.gift_shop_backend.services.CartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("User-ID") String userId,
            @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    /**
     * Remove a product entirely from the shopping card of a user
     * @param userId: The user id of the owner of the shopping cart
     * @param productId: The product to be removed.
     * @return Empty Http entity with "no content" header if found otherwise with "not found" header.
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("User-ID") String userId,
            @PathVariable UUID productId
    ) {
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Get the collection of all cart items for a user.
     * @return Http entity containing all cart items for the given user.
     */
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("User-ID") String userId
    ){
      return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> clearCart(
            @RequestHeader("User-ID") String userId
    ){
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }


}
