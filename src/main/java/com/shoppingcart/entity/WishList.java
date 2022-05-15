package com.shoppingcart.entity;

import javax.persistence.*;

@Entity
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int wishId;
	
	public int getWishId() {
		return wishId;
	}

	public void setWishId(int wishId) {
		this.wishId = wishId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(length = 50)
	private String displayName;
	@Column(length = 50)
	private String shortDesc;
	@Column(length = 20)
	private String category;
}
