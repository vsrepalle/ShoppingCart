package com.shoppingcart.entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
public class Account {

	public Account(String name, Cart cart, List<WishList> wishList, String password, String email, List<Order> ordersList, String role, ShippingAddress shippingAddress) {
		this.name = name;
		this.cart = cart;
		this.wishList = wishList;
		this.password = password;
		this.email = email;
		this.ordersList = ordersList;
		this.role = role;
		this.shippingAddress = shippingAddress;
	}

	public Account() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Integer getId() {
		return id;
	}

	@Column(nullable = false)
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private Cart cart;

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(cascade = CascadeType.ALL)
	private List<WishList> wishList;

	

	public List<WishList> getWishList() {
		return wishList;
	}

	public void setWishList(List<WishList> wishList) {
		this.wishList = wishList;
	}

	//Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[#?!@$%^&*-]).{8,}$"
			,message="Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:")
	@Column(nullable = false)
	private String password;
	@Column(unique=true,nullable=false)
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Order> ordersList;

	@Column(columnDefinition = "enum(\"USER\",\"ADMIN\")",nullable = false)
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Order> getOrdersList() {
		return ordersList;
	}

	public void setOrdersList(List<Order> ordersList) {
		this.ordersList = ordersList;
	}

	@OneToOne(cascade = CascadeType.ALL)
	private ShippingAddress shippingAddress;



}
