package io.github.houcai.gift_shop_backend.repositories;

import io.github.houcai.gift_shop_backend.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
