package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
}
