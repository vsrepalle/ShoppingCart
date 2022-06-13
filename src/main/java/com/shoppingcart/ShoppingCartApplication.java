package com.shoppingcart;

import com.shoppingcart.service.impl.UserDetailsServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class ShoppingCartApplication {

	@Bean
	public UserDetailsService userDetailsService(){
		return new UserDetailsServiceImpl();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

}
