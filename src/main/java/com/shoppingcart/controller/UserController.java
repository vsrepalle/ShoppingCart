package com.shoppingcart.controller;

import com.shoppingcart.entity.User;
import com.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping(value="users")
	public ResponseEntity<?> getAllUsers(){
		return new ResponseEntity<>(shoppingCartService.getAllUsers(), HttpStatus.OK);
	}
	
	@GetMapping(value="users/{userId}")
	public ResponseEntity<?> getUser(@PathVariable int userId) {
		try {
			shoppingCartService.checkUser(userId);
			return new ResponseEntity<>(shoppingCartService.getUser(userId), HttpStatus.FOUND);
		}catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="users")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		try {
			return new ResponseEntity<>(shoppingCartService.addUser(user), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@DeleteMapping(value="users")
	public ResponseEntity<String> deleteUser(@RequestParam int userId){
		try {
			shoppingCartService.checkUser(userId);
			shoppingCartService.deleteUser(userId);
			return new ResponseEntity<>("Successfully removed the User", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}


}
