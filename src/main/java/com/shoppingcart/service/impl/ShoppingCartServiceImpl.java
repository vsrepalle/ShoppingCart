package com.shoppingcart.service.impl;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.Cart;
import com.shoppingcart.entity.Product;
import com.shoppingcart.exception.EmptyListOfProductsException;
import com.shoppingcart.exception.InvalidQuantityException;
import com.shoppingcart.exception.NoSuchProductException;
import com.shoppingcart.exception.ProductNotPresentInCartException;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private AccountRepository accountRepository;
	
	private final Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);


	@Override
	public List<Product> getProductsInCart(int cartId) {
		log.debug("Getting Products in Cart By Cart Id {}",cartId);
		Map<Integer,Integer> productQuantityMap = cartRepository.findById(cartId).get().getProductQuantityMap();
		List<Product> productsList = new ArrayList<>();
		for (Map.Entry<Integer,Integer> entry : productQuantityMap.entrySet()) {
			Product product = productRepository.findById(entry.getKey()).get();
			productsList.add(product);
		}
		return productsList;
	}
	private float calculateCartPrice(Cart cart, int productId, int modifiedQuantity) {
		log.debug("calculating the cart price with productId {} and modifiedQuantity {}",productId, modifiedQuantity);
		float productPrice = productRepository.findById(productId).get().getPrice();
		if(modifiedQuantity==0) {
			int quantity = cart.getProductQuantityMap().get(productId);
			return cart.getCartPrice()-(productPrice*quantity);
		}
		else {
			return cart.getCartPrice()+(productPrice*modifiedQuantity);
		}
	}
	private void checkProductInCart(Cart cart, int productId) throws ProductNotPresentInCartException {
		log.debug("checking the product in the cart with productId {}",productId);
		if(!cart.getProductQuantityMap().containsKey(productId)) {
			throw new ProductNotPresentInCartException();
		}
	}


	@Override
	public Cart addProductInCart(int cartId, int productId,int quantity) {
		log.debug("Adding Product with id {} in cart with id {}",productId,cartId);
		Cart cart = cartRepository.findById(cartId).get();
		try {
			Map<Integer,Integer> productQuantityMap = cart.getProductQuantityMap();
			productQuantityMap.put(productId, quantity);
			cart.setProductQuantityMap(productQuantityMap);
		} catch (ProductNotPresentInCartException e) {
			throw new NoSuchProductException();
		}
		cart.setCartPrice(this.calculateCartPrice(cart,productId,1));
		return cartRepository.save(cart);
	}


	@Override
	public Cart updateProductQuantityInCart(int cartId, int productId, int quantity)
			throws ProductNotPresentInCartException, InvalidQuantityException {
		log.debug("updating product with id {} in cart with id {} in quantity {}",productId,cartId,quantity);
		Cart cart = cartRepository.findById(cartId).get();
		this.checkProductInCart(cart, productId);
			int modifiedQuantity = quantity-cart.getProductQuantityMap().get(productId);
			if(quantity == 0) {
				Map<Integer,Integer> productQuantityMap = cart.getProductQuantityMap();
				productQuantityMap.replace(productId,quantity);
				cart.setProductQuantityMap(productQuantityMap);
				cart.setCartPrice(this.calculateCartPrice(cart, productId, quantity));
			}
			else if(quantity<0) {
				throw new InvalidQuantityException();
			}
			else if(modifiedQuantity!=0){
				cart.getProductQuantityMap().replace(productId, quantity);
				cart.setCartPrice(this.calculateCartPrice(cart, productId, modifiedQuantity));
			}
			return cartRepository.save(cart);
	}


	@Override
	public Cart removeProductFromCart(int cartId, int productId) throws ProductNotPresentInCartException {
		log.debug("remove form product with id{} in the cart id{}", cartId, productId);
		Cart cart = cartRepository.findById(cartId).get();
		this.checkProductInCart(cart, productId); //check if product is present in cart
		int quantity = cart.getProductQuantityMap().get(productId);
		Map<Integer,Integer> productQuantityMap = cart.getProductQuantityMap();
		productQuantityMap.remove(productId);
		cart.setProductQuantityMap(productQuantityMap);
		cart.setCartPrice(this.calculateCartPrice(cart, productId, quantity));
		return cartRepository.save(cart);
	}


	@Override
	public Cart removeAllProductsFromCart(int cartId) {
		log.debug("removing all products form cart Id{}",cartId);
		Cart cart = cartRepository.findById(cartId).get();
		cart.getProductQuantityMap().clear();
		cart.setCartPrice(0);
		return cartRepository.save(cart);
	}

	@Override
	public Cart getCart(int accountId) {
		log.debug("get all product's in cart with accountId {}",accountId);
		Optional<Account> account = accountRepository.findById(accountId);
		return account.map(Account::getCart).orElse(null);
	}

	@Override
	public List<Product> getAllProducts() {
		log.debug("get all products");
		return productRepository.findAll();
	}



	@Override
	public List<Product> searchProductsByCategory(String category) throws EmptyListOfProductsException {
		log.debug("search product by category",category);
		if(productRepository.findByCategory(category).isEmpty()) {
			throw new EmptyListOfProductsException();
		}
		return productRepository.findByCategory(category.toLowerCase());
	}

	@Override
	public void checkProduct(int productId) throws NoSuchProductException {
		log.debug("checking product with productId{}",productId);
		if(!productRepository.findById(productId).isPresent()) {
			throw new NoSuchProductException();
		}
	}

}
