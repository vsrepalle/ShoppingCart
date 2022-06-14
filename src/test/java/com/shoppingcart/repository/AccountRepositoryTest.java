package com.shoppingcart.repository;

import com.shoppingcart.entity.Account;
import com.shoppingcart.mapper.AccountMapper;
import com.shoppingcart.request.AccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@RunWith(SpringRunner.class)
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Find By Role")
    void findByRole() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Nani");
        accountRequest.setRole("USER");
        accountRequest.setEmail("nani@gmail.com");
        accountRequest.setPassword("Nani1234@");
        accountRequest.setConfirmPassword("Nani1234@");
        entityManager.persist(AccountMapper.mapToAccount(accountRequest));
        List<Account> accounts = accountRepository.findByRole("USER");
        assertNotNull(accounts.get(0));
    }

    @Test
    @DisplayName("Find By Email")
    void findByEmail() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Nani");
        accountRequest.setRole("USER");
        accountRequest.setEmail("nani@gmail.com");
        accountRequest.setPassword("Nani1234@");
        accountRequest.setConfirmPassword("Nani1234@");
    }
}