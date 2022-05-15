package com.shoppingcart.service;

import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Item;
import com.shoppingcart.entity.Product;
import com.shoppingcart.entity.User;
import com.shoppingcart.exception.*;

import java.util.List;

public interface ShoppingCartService {

	/*
	 * 
	 * User related
	 *  
	 */
	List<User> getAllUsers();
	User getUser(int userId) throws NoSuchUserException;
	User addUser(User user);
	void deleteUser(int userid) throws NoSuchUserException;
	void checkUser(int userId) throws NoSuchUserException;
	
	/*
	 * 
	 * Cart related 
	 * 
	 */
	List<Item> getProductsInCart(int cartId);
	Cart addProductInCart(int cartId, int productId);
	Cart updateProductQuantityInCart(int cartId, int productId,int quantity) throws ProductNotPresentInCartException, InvalidQuantityException;
	Cart removeProductFromCart(int cartId, int productId) throws ProductNotPresentInCartException; 
	Cart removeAllProductsFromCart(int cartId);
	Cart getCart(int cartId);
	
	/*
	 * 
	 * Product related
	 *  
	 */
	List<Product> getAllProducts() ;


	List<Product> searchProductsByCategory(String category) throws EmptyListOfProductsException;
	void checkProduct(int productId) throws NoSuchProductException;
}
