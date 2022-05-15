package com.shoppingcart.controller;

import com.shoppingcart.entity.Product;
import com.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	// Display all products
	@GetMapping(value = "products")
	public ResponseEntity<?> getAllProducts() {
		List<Product> products=shoppingCartService.getAllProducts();
		if(products.isEmpty()) {
			return new ResponseEntity<String>("No Products Available", HttpStatus.FOUND);
		}
			return new ResponseEntity<List<Product>>(products, HttpStatus.FOUND);
	}



	@GetMapping(value = "products/searchByCategory")
	public ResponseEntity<?> searchProductByCategory(@RequestParam String category) {
		try {
			List<Product> productList = shoppingCartService.searchProductsByCategory(category);
			return new ResponseEntity<List<Product>>(productList, HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
