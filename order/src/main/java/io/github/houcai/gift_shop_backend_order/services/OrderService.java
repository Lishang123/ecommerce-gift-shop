package io.github.houcai.gift_shop_backend_order.services;

import io.github.houcai.gift_shop_backend_order.clients.UserServiceClient;
import io.github.houcai.gift_shop_backend_order.dtos.OrderResponse;
import io.github.houcai.gift_shop_backend_order.dtos.UserResponse;
import io.github.houcai.gift_shop_backend_order.mappers.ResponseMapper;
import io.github.houcai.gift_shop_backend_order.models.CartItem;
import io.github.houcai.gift_shop_backend_order.models.Order;
import io.github.houcai.gift_shop_backend_order.models.OrderItem;
import io.github.houcai.gift_shop_backend_order.models.OrderStatus;
import io.github.houcai.gift_shop_backend_order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    @Transactional
    public Optional<OrderResponse> createOrder(String userId){
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        // Validate for user
        Optional<UserResponse> userOptional = userServiceClient.fetchUserById(Long.valueOf(userId));
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        // User user = userOptional.get();

        //TODO: reduce the quantity of the product for each order item.


        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                //.collect(Collectors.toList());
                .toList(); // more memory-efficient.

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(ResponseMapper.toResponse(savedOrder));
    }

}
