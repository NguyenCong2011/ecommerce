package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {

    Optional<ProductColor> findByValue(String value);
}
