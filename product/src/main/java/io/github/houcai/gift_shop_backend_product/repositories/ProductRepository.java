package io.github.houcai.gift_shop_backend_product.repositories;

import io.github.houcai.gift_shop_backend_product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByActiveTrue();
}
