package io.github.houcai.gift_shop_backend.repositories;

import io.github.houcai.gift_shop_backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
}
