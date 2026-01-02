package io.github.houcai.gift_shop_backend.repository;

import io.github.houcai.gift_shop_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
