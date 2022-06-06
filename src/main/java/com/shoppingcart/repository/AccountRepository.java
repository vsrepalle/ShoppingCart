package com.shoppingcart.repository;

import com.shoppingcart.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>  {
	List<Account> findByRole(String role);

	
	Optional<Account> findByEmail(String email);
}