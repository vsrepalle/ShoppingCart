package com.shoppingcart.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class Rating {
    @Id
    private int id;
    @Column
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column
    private int productId;
    @Column
    private Integer rating;
    @Column
    private String remarks;
}
