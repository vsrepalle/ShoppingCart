package com.shoppingcart.entity;

public enum ProductStatus {
    InStock("In Stock"),NoStock("No Stock");
    private String status;
    ProductStatus(String status){
        this.status=status;
    }
}
