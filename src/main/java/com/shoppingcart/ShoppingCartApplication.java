package com.shoppingcart;

import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ShoppingCartApplication implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            accountRepository.save(new Account
                    ("User", null, null, passwordEncoder().encode("User1234@"),
                            "user@gmail.com", null, "USER", null));
            accountRepository.save(new Account
                    ("Admin", null, null, passwordEncoder().encode("Admin1234@"), "admin@gmail.com", null, "ADMIN", null));
        }catch(Exception e){

        }
    }
}
