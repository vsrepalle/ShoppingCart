package com.shoppingcart.entity;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name="cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;

	@Column
	private float cartPrice;

	@ElementCollection
    @CollectionTable(name = "product_quantity_mapping",
      joinColumns = {@JoinColumn(name = "cartId")})
    @MapKeyJoinColumn(name = "product_id")
	@Column(name = "Quantity")
	private Map<Integer, Integer> productQuantityMap;

	public Map<Integer, Integer> getProductQuantityMap() {
		return productQuantityMap;
	}

	public void setProductQuantityMap(Map<Integer, Integer> productQuantityMap) {
		this.productQuantityMap = productQuantityMap;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public float getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(float cartPrice) {
		this.cartPrice = cartPrice;
	}

	@Override
	public String toString() {
		return "Cart [cartPrice=" + cartPrice + ", productQuantityMap="
				+ productQuantityMap + "]";
	}

}
