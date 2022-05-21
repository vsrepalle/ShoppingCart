package com.shoppingcart.controller;

import com.shoppingcart.entity.Product;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private ProductRepository productRepository;

	// Display all products
	@GetMapping(value = "products")
	public ResponseEntity<?> getAllProducts() {
		List<Product> products=shoppingCartService.getAllProducts();
		if(products.isEmpty()) {
			return new ResponseEntity<>("No Products Available", HttpStatus.NOT_FOUND);
		}
			return new ResponseEntity<>(products, HttpStatus.FOUND);
	}
	@PostMapping("product/add")
	public ResponseEntity<?> addProduct(Product product){
		productRepository.save(product);
		return new ResponseEntity<>("",HttpStatus.CREATED);
	}
	@PutMapping("/product/update/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId,@RequestBody Product productFromRequest){
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()){
			Product product=productOptional.get();
			product.setCategory(productFromRequest.getCategory());
			product.setPrice(productFromRequest.getPrice());
			product.setProdName(productFromRequest.getProdName());
			productRepository.save(product);
			return new ResponseEntity<>("Product Updated Successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>("Product Not Found With Id "+productId,HttpStatus.OK);
	}
	@DeleteMapping("product/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") Integer productId){
		try{
			productRepository.deleteById(productId);
			return new ResponseEntity<>("Deleted Product With Id "+productId,HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>("Product Not Found With Id "+productId,HttpStatus.NOT_FOUND);
		}
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
