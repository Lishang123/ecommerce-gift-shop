package io.github.houcai.gift_shop_backend.services;

import io.github.houcai.gift_shop_backend.dtos.CartItemRequest;
import io.github.houcai.gift_shop_backend.models.CartItem;
import io.github.houcai.gift_shop_backend.models.Product;
import io.github.houcai.gift_shop_backend.models.User;
import io.github.houcai.gift_shop_backend.repositories.CartItemRepository;
import io.github.houcai.gift_shop_backend.repositories.ProductRepository;
import io.github.houcai.gift_shop_backend.repositories.UserRepository;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public String addToCart(String userId, CartItemRequest request){
        // Look for product
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty())
            return "The product is not found.";

        Product product = productOpt.get();
        if (!product.getActive())
            return "The product is not active.";

        // Check for quantity
        if (product.getStockQuantity() < request.getQuantity())
            return "The product doesn't have the requested quantity.";

        // Look for User
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty())
            return "The user is not found";

        // Look for the cart item for this user and product.
        User user = userOpt.get();
        // If there is already an item in the cart: Update the quantity.
        if (cartItemRepository.findByUserAndProduct(user, product).isPresent()){
            CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product).get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else{
            // Create a new cart item.
            CartItem cartItem = new CartItem(
                    user,
                    product,
                    request.getQuantity(),
                    product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return "Added to shopping cart.";
    }

    @Transactional
    public boolean deleteItemFromCart(String userId, UUID productId){
        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        Optional<Product> product = productRepository.findById(productId);
        if (user.isPresent() && product.isPresent() && cartItemRepository.findByUserAndProduct(user.get(), product.get()).isPresent()){
            cartItemRepository.deleteByUserAndProduct(user.get(), product.get());
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId){
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                //.orElse(new ArrayList<>()); //eager
                .orElseGet(List::of); // lazy
    }

    public void clearCart(String userId){
        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
    }

}
