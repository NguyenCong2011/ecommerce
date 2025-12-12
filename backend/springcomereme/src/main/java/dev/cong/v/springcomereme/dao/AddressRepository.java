package dev.cong.v.springcomereme.dao;

import dev.cong.v.springcomereme.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
