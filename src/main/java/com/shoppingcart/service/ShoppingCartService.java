package com.shoppingcart.service;

import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Product;
import com.shoppingcart.exception.EmptyListOfProductsException;
import com.shoppingcart.exception.InvalidQuantityException;
import com.shoppingcart.exception.NoSuchProductException;
import com.shoppingcart.exception.ProductNotPresentInCartException;

import java.util.List;

public interface ShoppingCartService {
	/*
	 * 
	 * Cart related 
	 * 
	 */
	List<Product> getProductsInCart(int cartId);
	Cart addProductInCart(int cartId, int productId,int quantity);
	Cart updateProductQuantityInCart(int cartId, int productId,int quantity) throws ProductNotPresentInCartException, InvalidQuantityException;
	Cart removeProductFromCart(int cartId, int productId) throws ProductNotPresentInCartException; 
	Cart removeAllProductsFromCart(int cartId);
	Cart getCart(int accountId);
	
	/*
	 * 
	 * Product related
	 *  
	 */
	List<Product> getAllProducts() ;


	List<Product> searchProductsByCategory(String category) throws EmptyListOfProductsException;
	void checkProduct(int productId) throws NoSuchProductException;
}
