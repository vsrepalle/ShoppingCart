package com.shoppingcart.service.impl;

import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	log.debug("load by user ByUsername with email",email);
        Optional<Account> account = accountRepository.findByEmail(email);
        if(!account.isPresent()){
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserDetailsImpl(account.get());
    }
}
