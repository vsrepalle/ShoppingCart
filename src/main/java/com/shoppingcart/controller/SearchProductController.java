package com.shoppingcart.controller;

import com.shoppingcart.entity.Product;
import com.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "search")
public class SearchProductController {

	@Autowired
	private ProductRepository productRepository;
	

	@GetMapping(value = "/productByName/{prodName}")
	public ResponseEntity<?> getProductByName(@PathVariable String prodName) {
		try {
			List<Product> product = productRepository.findByProductName(prodName);

			if (product == null) {
				return new ResponseEntity<String>("No Product is found with product name :" + prodName, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(product, HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(value = "/productById/{prodId}")
	public ResponseEntity<?> getProductById(@PathVariable Integer prodId) {
		try {

			Optional<Product> product = productRepository.findById(prodId);

			if (!product.isPresent()) {
				return new ResponseEntity<>("No Product is found with product with ID :" + prodId, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(product.get(), HttpStatus.FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

}
