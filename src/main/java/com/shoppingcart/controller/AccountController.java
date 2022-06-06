package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AccountController {

    private AccountRepository accountRepository;

    @GetMapping("/account/findAll")
    public ResponseEntity<?> getAllAccounts(){
        return new ResponseEntity<>(accountRepository.findByRole("USER"), HttpStatus.OK);
    }
    @PostMapping("/account/makeAdmin/{accountId}")
    public ResponseEntity<?> makeAdmin(@PathVariable("accountId") Integer accountId){
        Optional<Account> accountOptional=accountRepository.findById(accountId);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setRole("ADMIN");
            accountRepository.save(account);
            return new ResponseEntity<>("USER is successfully ADMIN", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account Not Found With id "+accountId,HttpStatus.NOT_FOUND);
    }

}
