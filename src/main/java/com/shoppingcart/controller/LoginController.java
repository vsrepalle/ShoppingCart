package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "account")
public class LoginController {
	@Autowired
	private AccountUtil accountUtil;

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody Account account) {
		return accountUtil.validateAccount(account);
	}
}
