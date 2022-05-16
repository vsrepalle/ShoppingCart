package com.shoppingcart.utils;

import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountUtil {
    @Autowired
    private AccountRepository accountRepository;
    public ResponseEntity<String> validateAccount(Account account){
        String accountName = account.getName();
        if (null != accountName && accountName.isEmpty()) {
            return new ResponseEntity<>("User Name is not entered", HttpStatus.BAD_REQUEST);
        }

        else if( accountName == null) {
            return new ResponseEntity<>("Account Name is missing", HttpStatus.BAD_REQUEST);
        }

        else {
            Optional<Account> accountExists = accountRepository.findByName(account.getName());

            if(accountExists.isPresent()) {
                if(account.getPassword() != null && account.getPassword().equals(accountExists.get().getPassword())
                        && account.getPassword().equals(account.getConfirmPassword())) {
                    return new ResponseEntity<>("User Logged In!", HttpStatus.OK);
                }
            }

            else {
                return new ResponseEntity<>("User is not Registered", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
    }
}
