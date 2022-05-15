package com.shoppingcart.repository;

import com.shoppingcart.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress,Integer> {
}
