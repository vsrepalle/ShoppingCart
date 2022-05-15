package com.shoppingcart.repository;

import com.shoppingcart.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

}
