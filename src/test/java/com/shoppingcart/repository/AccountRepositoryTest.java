package com.shoppingcart.repository;

import com.shoppingcart.entity.Account;
import com.shoppingcart.mapper.AccountMapper;
import com.shoppingcart.request.AccountRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryTest {



    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Find By Role")
    @Order(1)
    void findByRole() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Nani");
        accountRequest.setRole("USER");
        accountRequest.setEmail("nani@gmail.com");
        accountRequest.setPassword("Nani1234@");
        accountRequest.setConfirmPassword("Nani1234@");
        accountRepository.save(AccountMapper.mapToAccount(accountRequest));
        List<Account> accounts = accountRepository.findByRole("USER");
        assertNotNull(accounts.get(0));
    }

    @Test
    @DisplayName("Find By Email")
    @Order(2)
    void findByEmail() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Nani");
        accountRequest.setRole("USER");
        accountRequest.setEmail("nani@gmail.com");
        accountRequest.setPassword("Nani1234@");
        accountRequest.setConfirmPassword("Nani1234@");
        accountRepository.save(AccountMapper.mapToAccount(accountRequest));
        Optional<Account> account = accountRepository.findByEmail(accountRequest.getEmail());
        assertTrue(account.isPresent());
    }
}