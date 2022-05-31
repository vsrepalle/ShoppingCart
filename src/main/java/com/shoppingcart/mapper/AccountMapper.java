package com.shoppingcart.mapper;

import com.shoppingcart.entity.Account;
import com.shoppingcart.request.AccountRequest;

public class AccountMapper {
    public static Account mapToAccount(AccountRequest accountRequest){
        Account account = new Account();
        account.setPassword(accountRequest.getPassword());
        account.setEmail(accountRequest.getEmail());
        account.setName(accountRequest.getName());
        account.setRole(accountRequest.getRole());
        return account;
    }
}
