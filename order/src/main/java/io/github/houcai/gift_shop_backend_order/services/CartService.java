package io.github.houcai.gift_shop_backend_order.services;

import io.github.houcai.gift_shop_backend_order.clients.ProductServiceClient;
import io.github.houcai.gift_shop_backend_order.clients.UserServiceClient;
import io.github.houcai.gift_shop_backend_order.dtos.CartItemRequest;
import io.github.houcai.gift_shop_backend_order.dtos.ProductResponse;
import io.github.houcai.gift_shop_backend_order.dtos.UserResponse;
import io.github.houcai.gift_shop_backend_order.models.CartItem;
import io.github.houcai.gift_shop_backend_order.repositories.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name="orderServiceBreaker", fallbackMethod = "")
    public String addToCart(String userId, CartItemRequest request){
        // Look for product
        Optional<ProductResponse> productOpt = productServiceClient.fetchProductById(request.getProductId());
        if (productOpt.isEmpty())
            return "The product is not found.";

        ProductResponse product = productOpt.get();
        if (!product.getActive())
            return "The product is not active.";

        // Check for quantity
        if (product.getStockQuantity() < request.getQuantity())
            return "The product doesn't have the requested quantity.";

        // Look for User
        Optional<UserResponse> userOpt = userServiceClient.fetchUserById(Long.valueOf(userId));
        if (userOpt.isEmpty())
            return "The user is not found";

        // Look for the cart item for this user and product.
        UUID productId = request.getProductId();
        // If there is already an item in the cart: Update the quantity.
        if (cartItemRepository.findByUserIdAndProductId(userId, productId).isPresent()){
            CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, productId).get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else{
            // Create a new cart item.
            CartItem cartItem = new CartItem(
                    userId,
                    productId,
                    request.getQuantity(),
                    BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }

        return "Added to shopping cart.";
    }

    @Transactional
    public boolean deleteItemFromCart(String userId, UUID productId){
        if (cartItemRepository.findByUserIdAndProductId(userId, productId).isPresent()){
            cartItemRepository.deleteByUserIdAndProductId(userId, productId);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId){
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId){
        cartItemRepository.deleteByUserId(userId);
    }

}
