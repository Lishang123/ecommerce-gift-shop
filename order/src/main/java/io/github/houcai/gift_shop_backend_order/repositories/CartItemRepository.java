package io.github.houcai.gift_shop_backend_order.repositories;

import io.github.houcai.gift_shop_backend_order.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByUserIdAndProductId(String userId, UUID productId);

    void deleteByUserIdAndProductId(String userId, UUID productId);

    List<CartItem> findByUserId(String userId);

    void deleteByUserId(String userId);
}
