package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {

}
