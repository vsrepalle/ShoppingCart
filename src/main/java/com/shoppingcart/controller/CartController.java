package com.shoppingcart.controller;

import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Item;
import com.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "users")
public class CartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping(value = "/{accountId}/cart")
	public ResponseEntity<?> getCart(@PathVariable int accountId){
		try {
			shoppingCartService.checkUser(accountId);
			return new ResponseEntity<>(shoppingCartService.getCart(accountId), HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}

	@GetMapping(value = "{accountId}/cart/products")
	public ResponseEntity<?> getProductsInCart(@PathVariable("accountId") int accountId){
		try {
			shoppingCartService.checkUser(accountId);
			List<Item> itemList = shoppingCartService.getProductsInCart(accountId);
			return new ResponseEntity<>(itemList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(value = "/{accountId}/cart/products")
	public ResponseEntity<?> addProductInCart(
			@PathVariable("accountId") int accountId, 
			@RequestParam int productId){
		try {
			shoppingCartService.checkUser(accountId);
			shoppingCartService.checkProduct(productId);
			Cart cart = shoppingCartService.addProductInCart(accountId, productId);
			return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping(value = "/{accountId}/cart/products/{productId}")
	public ResponseEntity<?> updateProductQuantityInCart(
			@PathVariable("accountId") int accountId,
			@PathVariable("productId") int productId,
			@RequestParam int quantity){
		try {
			shoppingCartService.checkUser(accountId);
			shoppingCartService.checkProduct(productId);
			Cart cart = shoppingCartService.updateProductQuantityInCart(accountId, productId, quantity);
			return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}
	@DeleteMapping(value = "/{accountId}/cart/products")
	public ResponseEntity<?> removeProductFromCart(
			@PathVariable("accountId") int accountId,
			@RequestParam int productId){
		try {
			shoppingCartService.checkUser(accountId);
			shoppingCartService.checkProduct(productId);
			Cart cart = shoppingCartService.removeProductFromCart(accountId, productId);
			return new ResponseEntity<>(cart, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}	
	}
	@DeleteMapping(value = "/{accountId}/cart")
	public ResponseEntity<?> removeAllProductsFromCart(@PathVariable("accountId") int accountId){
		try {
			shoppingCartService.checkUser(accountId);
			Cart cart = shoppingCartService.removeAllProductsFromCart(accountId);
			return new ResponseEntity<>(cart, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
