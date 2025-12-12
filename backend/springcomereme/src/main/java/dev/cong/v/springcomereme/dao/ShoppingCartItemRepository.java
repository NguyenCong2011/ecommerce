package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem,Long> {
    Optional<ShoppingCartItem> findByShoppingCartIdAndProductItemId(Long cartId, Long productItemId);

}
