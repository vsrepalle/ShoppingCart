package com.shoppingcart.dto;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.Order;

public class OrderDTO {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private Account account;
}
