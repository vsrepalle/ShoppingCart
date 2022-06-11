package com.shoppingcart.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name= "order_new")
public class Order {

	@Id
	private String orderId;

	public Order(String orderId, String displayName, String category, String sellerName, float price, int quantity, float totalPrice, Date orderedDate, String orderStatus) {
		this.orderId = orderId;
		this.displayName = displayName;
		this.category = category;
		this.sellerName = sellerName;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.orderedDate = orderedDate;
		this.orderStatus = orderStatus;
	}
    public Order() {
    	
    }

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}



	public float getTotalPrice() {
		return totalPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}



	public String getOrderStatus() {
		return orderStatus;
	}

	public Date getOrderedDate() {
		return orderedDate;
	}

	public void setOrderedDate(Date orderedDate) {
		this.orderedDate = orderedDate;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	private String displayName;

	private Integer userId;

	private String category;
	
	private String sellerName;
	
	private float price;
	
	private int quantity;
	
	private float totalPrice;
	
	private Date orderedDate;

	private String orderStatus;

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	private Integer cartId;
}
