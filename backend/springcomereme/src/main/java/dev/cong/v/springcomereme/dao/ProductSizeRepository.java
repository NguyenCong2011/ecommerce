package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize,Long> {

    Optional<ProductSize> findByValue(String value);
}
