package com.shoppingcart.controller;

import com.shoppingcart.entity.Product;
import com.shoppingcart.entity.ProductStatus;
import com.shoppingcart.entity.Rating;
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
		int stockQty = product.getStockQty();

			if(stockQty >0){
				product.setStatus(ProductStatus.InStock);
			}
			else{
				product.setStatus(ProductStatus.NoStock);
			}


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

	@PutMapping("product/rate/{productId}/{rating}")
	public ResponseEntity<?> rateProduct(@PathVariable("productId") Integer productId, @RequestBody Rating rating){
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()){
			Product product=productOptional.get();

			Rating ratingInDB = product.getRating();
			ratingInDB.setRating(rating.getRating());
			ratingInDB.setProductId(rating.getProductId());
			ratingInDB.setUserId(rating.getUserId());
			ratingInDB.setRemarks(rating.getRemarks());

			product.setRating(rating);

			productRepository.save(product);
			return new ResponseEntity<>("Product rating Updated Successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>("Product Not Found With Id "+productId,HttpStatus.OK);
	}

}
