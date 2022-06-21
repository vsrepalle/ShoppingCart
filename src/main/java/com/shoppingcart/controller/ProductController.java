package com.shoppingcart.controller;

import com.shoppingcart.entity.Product;
import com.shoppingcart.entity.Rating;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.repository.RatingRepository;
import com.shoppingcart.service.ShoppingCartService;
import com.shoppingcart.utils.AccountUtil;
import com.shoppingcart.utils.BasicAuthenticationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountUtil accountUtil;

	private final Logger log = Logger.getLogger(ProductController.class.getName());
    @Autowired
    private RatingRepository ratingRepository;

    @GetMapping(value = "products")
    public ResponseEntity<?> getAllProducts() {
        log.debug("Getting All the products");
        List<Product> products = shoppingCartService.getAllProducts();
        if (products.isEmpty()) {
            log.debug("There is no products in the database");
            return new ResponseEntity<>("No Products Available", HttpStatus.NOT_FOUND);
        }
        log.debug(products.size() == 1 ? "1 product is in the database" : products.size() + " products is in the database");
        return new ResponseEntity<>(products, HttpStatus.FOUND);
    }

    @PostMapping("product/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        int stockQty = product.getStockQty();
        if (stockQty > 0) {
            log.debug("Product is in stock");
            product.setStatus("In Stock");
        } else {
            log.debug("Product is not in stock");
            if(stockQty < 0){
                product.setStockQty(0);
            }
            product.setStatus("No Stock");
        }


        productRepository.save(product);
        return new ResponseEntity<>("Product Added Successfully", HttpStatus.CREATED);
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Integer productId, @RequestBody Product productFromRequest) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setCategory(productFromRequest.getCategory());
            product.setPrice(productFromRequest.getPrice());
            product.setProdName(productFromRequest.getProdName());
            if(productFromRequest.getStockQty() < 0){
                productFromRequest.setStockQty(0);
            }
            product.setStockQty(productFromRequest.getStockQty());
            product.setStatus(productFromRequest.getStockQty() > 0?"In Stock":"No Stock");
            productRepository.save(product);
            log.debug("Product updated Successfully with id " + productId);
            return new ResponseEntity<>("Product Updated Successfully", HttpStatus.OK);
        }
        log.error("Product Not Found With Id " + productId);
        return new ResponseEntity<>("Product Not Found With Id " + productId, HttpStatus.OK);
    }

    @DeleteMapping("product/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Integer productId) {
        try {
            productRepository.deleteById(productId);
            log.debug("Deleted Product With Id " + productId);
            return new ResponseEntity<>("Deleted Product With Id " + productId, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Product Not Found With Id " + productId, e);
            return new ResponseEntity<>("Product Not Found With Id " + productId, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "products/searchByCategory")
    public ResponseEntity<?> searchProductByCategory(@RequestParam String category) {
        log.debug("Searching Products with category " + category);
        try {
            List<Product> productList = shoppingCartService.searchProductsByCategory(category);
            log.debug("List is not empty when search with category");
            return new ResponseEntity<>(productList, HttpStatus.FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/topRated")
    public ResponseEntity<?> topRated() {
        return new ResponseEntity<>(ratingRepository.findByRatingOrderByRatingDesc().stream().limit(10).collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @PutMapping("product/rate/{productId}")
    public ResponseEntity<?> rateProduct(@PathVariable("productId") Integer productId, @RequestBody Rating rating,@RequestHeader("Authorization") String authorization) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            int userId=accountUtil.getAccount(BasicAuthenticationUtil.getCredentials(authorization)).getId();
            Product product = productOptional.get();
            rating.setUserId(userId);
            List<Rating> ratings = product.getRatings();
            if(ratings.stream().anyMatch(ratingFromDB -> ratingFromDB.getUserId()==userId)){
                Rating ratingAlreadyExistsWithUserId = ratings.stream().filter(ratingFromDB -> ratingFromDB.getUserId()==userId)
                        .collect(Collectors.toList())
                        .get(0);
                ratingAlreadyExistsWithUserId.setRating(rating.getRating());
                ratingAlreadyExistsWithUserId.setRemarks(rating.getRemarks());
                ratingRepository.save(ratingAlreadyExistsWithUserId);
                log.debug("Product rating Updated Successfully with id " + productId);
                return new ResponseEntity<>("Product Rating Updated Successfully",HttpStatus.OK);
            }
            ratings.add(rating);
            product.setRatings(ratings);
            productRepository.save(product);
            log.debug("Product rating Updated Successfully with id " + productId);
            return new ResponseEntity<>("Product rating Updated Successfully", HttpStatus.OK);
        }
        log.error("Product Not Found With Id " + productId);
        return new ResponseEntity<>("Product Not Found With Id " + productId, HttpStatus.OK);
    }



    @GetMapping("/product/lowStockProduct")
    public ResponseEntity<?> getLowStockProduct() {
        return new ResponseEntity<>(productRepository.getLowStockProduct().get(0), HttpStatus.OK);
    }

}
