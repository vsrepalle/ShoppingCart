package com.shoppingcart.controller;

import com.shoppingcart.dto.CheckoutDTO;
import com.shoppingcart.entity.Order;
import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.ShippingAddressRepository;
import com.shoppingcart.utils.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CheckoutController {

 
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private AccountUtil accountUtil;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/checkout/{shippingAddressId}")
    public ResponseEntity<?> checkout(@PathVariable("shippingAddressId") Integer shippingAddressId,
                                      @RequestBody CheckoutDTO checkoutDTO){
        Optional<ShippingAddress> shippingAddressOptional = shippingAddressRepository.findById(shippingAddressId);
        if(!shippingAddressOptional.isPresent()){
            return new ResponseEntity<>("Shipping Address not found with id "+shippingAddressId,HttpStatus.NOT_FOUND);
        }
        ResponseEntity<String> validateAccount = accountUtil.validateAccount(checkoutDTO.getAccount());
        if(validateAccount.getStatusCode().equals(HttpStatus.OK)) {
        	Order order = checkoutDTO.getOrder();
        	order.setOrderId(java.util.UUID.randomUUID().toString());
            orderRepository.save(order);
            return new ResponseEntity<>(checkoutDTO.getOrder().getOrderId(), HttpStatus.OK);
        }
        return validateAccount;
    }
}
