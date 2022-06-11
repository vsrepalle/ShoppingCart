package com.shoppingcart.controller;

import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Product;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.service.ShoppingCartService;
import org.apache.log4j.Logger;
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
	@Autowired
	private AccountRepository accountRepository;
	private final Logger log = Logger.getLogger(CartController.class);


	@GetMapping(value = "/{accountId}/cart")
	public ResponseEntity<?> getCart(@PathVariable int accountId){
		log.debug("Getting Cart with account id "+accountId);
		try {
			log.debug("Getting Cart is Success");
			return new ResponseEntity<>(shoppingCartService.getCart(accountId), HttpStatus.FOUND);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}

	@GetMapping(value = "{accountId}/cart/products")
	public ResponseEntity<?> getProductsInCart(@PathVariable("accountId") int accountId){
		log.debug("Getting All the products with account id "+accountId);
		try {
			List<Product> products = shoppingCartService.getProductsInCart(accountId);
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping(value = "/{accountId}/cart/products")
	public ResponseEntity<?> addProductInCart(
			@PathVariable("accountId") int accountId, 
			@RequestParam int productId){
		log.debug("adding products in cart with accountId "+accountId+" and productId "+productId);
		try {
			shoppingCartService.checkProduct(productId);
			Cart cart = shoppingCartService
					.addProductInCart(accountRepository.findById(accountId).get().getCart().getCartId(), productId);
			return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping(value = "/{accountId}/cart/products/{productId}")
	public ResponseEntity<?> updateProductQuantityInCart(
			@PathVariable("accountId") int accountId,
			@PathVariable("productId") int productId,
			@RequestParam int quantity){
		log.debug("update the product quantityInCart with accountId"+accountId+" and productId "+productId+" and quantity"+quantity);
		try {
			shoppingCartService.checkProduct(productId);
			log.debug("checking the product with productId "+productId);
			Cart cart = shoppingCartService.updateProductQuantityInCart(accountId, productId, quantity);
			log.debug("Successfully updated by productQuantity in cart");
			return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
		} catch(Exception e) {
		log.error(e.getMessage(),e);		
	return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}
	@DeleteMapping(value = "/{accountId}/cart/products")
	public ResponseEntity<?> removeProductFromCart(
			@PathVariable("accountId") int accountId,
			@RequestParam int productId){
		log.debug("deleting product's in cart with accountId "+accountId+" and productId "+productId);
		try {
			shoppingCartService.checkProduct(productId);
			log.debug("checking the product in cart with productId "+productId);
			Cart cart = shoppingCartService.removeProductFromCart(accountId, productId);
			log.debug("Successfully deleted product from cart");
			return new ResponseEntity<>(cart, HttpStatus.OK);
		} catch(Exception e) {
		log.error(e.getMessage(),e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}	
	}
	@DeleteMapping(value = "/{accountId}/cart")
	public ResponseEntity<?> removeAllProductsFromCart(@PathVariable("accountId") int accountId){
		log.debug("deleting all product's from cart "+accountId);
		try {
			Cart cart = shoppingCartService.removeAllProductsFromCart(accountId);
			log.debug("Successfully deleted the all products in cart");
			return new ResponseEntity<>(cart, HttpStatus.OK);
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
