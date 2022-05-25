package com.shoppingcart.entity;

import javax.persistence.*;

@Entity
public class Product {

	@Id
	@Column
	private int productId;
	@Column
	private String prodName;
	@Column
	private float price;
	@Column
	private String category;

	public int getStockQty() {
		return stockQty;
	}

	public void setStockQty(int stockQty) {
		this.stockQty = stockQty;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}


	@OneToOne(cascade = CascadeType.ALL)
	private Rating rating;

	@Column
	private int stockQty;

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	public Product(int productId, String prodName, float price, String category) {
		this.productId = productId;
		this.prodName = prodName;
		this.price = price;
		this.category = category;
	}

	public Product() {

	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Product [Id=" + productId + ", Name=" + prodName + ", price=" + price + ", category="
				+ category + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 100;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((prodName == null) ? 0 : prodName.hashCode());
		result = prime * result + productId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (prodName == null) {
			if (other.prodName != null)
				return false;
		} else if (!prodName.equals(other.prodName))
			return false;
		if (productId != other.productId)
			return false;
		return true;
	}
	
}
