package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
