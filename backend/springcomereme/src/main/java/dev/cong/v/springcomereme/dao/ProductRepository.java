package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {


    @Query(value = "SELECT * FROM product WHERE product.id != :id ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Product> findRandomProducts(@Param("id") Long id);


    List<Product> findByShowHomepageTrue();

}
