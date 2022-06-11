package com.shoppingcart.utils;

import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountUtil {

    private final AccountRepository accountRepository;

    public AccountUtil(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String[] credentials){
        Optional<Account> accountOptional = accountRepository.findByEmail(credentials[0]);
        return accountOptional.get();
    }
}
