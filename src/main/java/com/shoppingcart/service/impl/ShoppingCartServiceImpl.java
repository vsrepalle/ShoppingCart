package com.shoppingcart.service.impl;

import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Item;
import com.shoppingcart.entity.Product;
import com.shoppingcart.entity.User;
import com.shoppingcart.exception.*;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.repository.UserRepository;
import com.shoppingcart.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<User> getAllUsers() {
		log.debug("Getting All Users");
		return userRepository.findAll();
	}

	@Override
	public User getUser(int userId) {
		log.debug("Getting User with id {}",userId);
		return userRepository.findById(userId).get();
	}


	@Override
	public User addUser(User user) {
		log.debug("Saving User in Database");
		return userRepository.save(user);
	}


	@Override
	public void deleteUser(int userId) {
		log.debug("Deleting User with id {}",userId);
		try{
			userRepository.deleteById(userId);
		}catch(Exception e){
			log.error("Cannot delete with User with id {}",userId);
			throw new NoSuchUserException();
		}
	}

	@Override
	public void checkUser(int userId) throws NoSuchUserException {
		log.debug("Checking User with id {}",userId);
		if(!userRepository.findById(userId).isPresent()) {
			throw new NoSuchUserException();
		}
	}

	@Override
	public List<Item> getProductsInCart(int cartId) {
		log.debug("Getting Products in Cart By Cart Id {}",cartId);
		Map<Integer,Integer> productQuantityMap = cartRepository.findById(cartId).get().getProductQuantityMap();
		List<Item> itemList = new ArrayList<>();
		for (Map.Entry<Integer,Integer> entry : productQuantityMap.entrySet()) {
			Item item = new Item(productRepository.findById(entry.getKey()).get(),entry.getValue());
			itemList.add(item);
		}
		return itemList;
	}
	private float calculateCartPrice(Cart cart, int productId, int modifiedQuantity) {
		float productPrice = productRepository.findById(productId).get().getPrice();
		if(modifiedQuantity==0) {
			int quantity = cart.getProductQuantityMap().get(productId);
			return cart.getCartPrice()-(productPrice*quantity);
		}
		else {
			return cart.getCartPrice()+(productPrice*modifiedQuantity);
		}
	}
	private boolean checkProductInCart(Cart cart, int productId) throws ProductNotPresentInCartException {
		if(cart.getProductQuantityMap().containsKey(productId)) {
			return true;
		}
		else {
			throw new ProductNotPresentInCartException();
		}
	}


	@Override
	public Cart addProductInCart(int cartId, int productId) {
		log.debug("Adding Product with id {} in cart with id {}",productId,cartId);
		Cart cart = cartRepository.findById(cartId).get();
		try {
			this.checkProductInCart(cart, productId);
			int initialQuantity = cart.getProductQuantityMap().get(productId);
			cart.getProductQuantityMap().replace(productId, initialQuantity+1);
		} catch (ProductNotPresentInCartException e) {
			cart.getProductQuantityMap().put(productId,1);
		}
		cart.setCartPrice(this.calculateCartPrice(cart,productId,1));
		return cartRepository.save(cart);
	}


	@Override
	public Cart updateProductQuantityInCart(int cartId, int productId, int quantity)
			throws ProductNotPresentInCartException, InvalidQuantityException {
		Cart cart = cartRepository.findById(cartId).get();
		if(this.checkProductInCart(cart, productId)) { //check if product is present in cart
			int modifiedQuantity = quantity-cart.getProductQuantityMap().get(productId);
			if(quantity == 0) {
				cart.setCartPrice(this.calculateCartPrice(cart, productId, quantity));
				cart.getProductQuantityMap().remove(productId);
			}
			else if(quantity<0) {
				throw new InvalidQuantityException();
			}
			else if(quantity!=0 && modifiedQuantity!=0){
				cart.getProductQuantityMap().replace(productId, quantity);
				cart.setCartPrice(this.calculateCartPrice(cart, productId, modifiedQuantity));
			}
			return cartRepository.save(cart);
		}
		else {
			throw new ProductNotPresentInCartException();
		}
	}


	@Override
	public Cart removeProductFromCart(int cartId, int productId) throws ProductNotPresentInCartException {
		Cart cart = cartRepository.findById(cartId).get();
		this.checkProductInCart(cart, productId); //check if product is present in cart
		int quantity = cart.getProductQuantityMap().get(productId);
		cart.setCartPrice(this.calculateCartPrice(cart, productId, -quantity));
		cart.getProductQuantityMap().remove(productId);
		return cartRepository.save(cart);
	}


	@Override
	public Cart removeAllProductsFromCart(int cartId) {
		Cart cart = cartRepository.findById(cartId).get();
		cart.getProductQuantityMap().clear();
		cart.setCartPrice(0);
		return cartRepository.save(cart);
	}

	@Override
	public Cart getCart(int cartId) {
		return cartRepository.findById(cartId).get();
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}



	@Override
	public List<Product> searchProductsByCategory(String category) throws EmptyListOfProductsException {
		if(productRepository.findByCategory(category).isEmpty()) {
			throw new EmptyListOfProductsException();
		}
		return productRepository.findByCategory(category.toLowerCase());
	}

	@Override
	public void checkProduct(int productId) throws NoSuchProductException {
		if(!productRepository.findById(productId).isPresent()) {
			throw new NoSuchProductException();
		}
	}

}
