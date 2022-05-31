package com.shoppingcart.repository;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>  {

	
	Optional<Account> findByEmail(String email);
}