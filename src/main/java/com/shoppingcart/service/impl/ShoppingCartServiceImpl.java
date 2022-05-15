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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(int userId) {
		return userRepository.findById(userId).get();
	}

	@Transactional(rollbackFor = InternalErrorException.class)
	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}


	@Override
	public void deleteUser(int userId) {
		try{
			userRepository.deleteById(userId);
		}catch(Exception e){
			throw new NoSuchUserException();
		}
	}

	@Override
	public void checkUser(int userId) throws NoSuchUserException {
		if(!userRepository.findById(userId).isPresent()) {
			throw new NoSuchUserException();
		}
	}

	@Override
	public List<Item> getProductsInCart(int cartId) throws InternalErrorException {
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

	@Transactional(rollbackFor = InternalErrorException.class)
	@Override
	public Cart addProductInCart(int cartId, int productId) {
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

	@Transactional(rollbackFor = InternalErrorException.class)
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

	@Transactional(rollbackFor = InternalErrorException.class)
	@Override
	public Cart removeProductFromCart(int cartId, int productId) throws ProductNotPresentInCartException {
		Cart cart = cartRepository.findById(cartId).get();
		this.checkProductInCart(cart, productId); //check if product is present in cart
		int quantity = cart.getProductQuantityMap().get(productId);
		cart.setCartPrice(this.calculateCartPrice(cart, productId, -quantity));
		cart.getProductQuantityMap().remove(productId);
		return cartRepository.save(cart);
	}

	@Transactional(rollbackFor = InternalErrorException.class)
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