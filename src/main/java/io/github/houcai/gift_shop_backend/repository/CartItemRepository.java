package io.github.houcai.gift_shop_backend.repository;

import io.github.houcai.gift_shop_backend.model.CartItem;
import io.github.houcai.gift_shop_backend.model.Product;
import io.github.houcai.gift_shop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
