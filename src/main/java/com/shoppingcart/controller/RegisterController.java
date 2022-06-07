package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.mapper.AccountMapper;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.request.AccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "users")

public class RegisterController {

    @Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final Logger log = LoggerFactory.getLogger(RegisterController.class);

	@PostMapping(value = "/account")
	public ResponseEntity<?> register(@RequestBody AccountRequest account) {
		log.debug("adding user details into the account",account);
		if (null != account.getName() && !isValidUsername(account.getName())) {
			return new ResponseEntity<>("Name Shouldn't contain anything other than alphabets", HttpStatus.BAD_REQUEST);
		}
		if (account.getEmail() == null || account.getEmail().isEmpty()) {
			return new ResponseEntity<>("Email Is Required", HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountFromDB = accountRepository.findByEmail(account.getEmail());
		if (!accountFromDB.isPresent()) {

			if (account.getName() == null || account.getEmail() == null || account.getPassword() == null ||
					account.getConfirmPassword() == null ||
					account.getName().isEmpty() || account.getEmail().isEmpty() || account.getPassword().isEmpty() ||
					account.getConfirmPassword().isEmpty()) {
				return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);

			}

			if (account.getPassword().equals(account.getConfirmPassword())) {
					account.setPassword(passwordEncoder.encode(account.getPassword()));
					accountRepository.save(AccountMapper.mapToAccount(account));
					if(account.getRole() != null) {
						return new ResponseEntity<>("Account Registered", HttpStatus.CREATED);
					}
					else {
						return new ResponseEntity<>("Enter role field to register",HttpStatus.BAD_REQUEST);
					}
			}
			return new ResponseEntity<>("Password and Confirm Password are not same", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Email Already Exists Please Log In", HttpStatus.BAD_REQUEST);
	}

	private static boolean isValidUsername(String name) {
		if (name == null) {
			return false;
		}
		String regex = "[a-zA-Z][a-zA-Z ]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		return m.matches();
	}
}
