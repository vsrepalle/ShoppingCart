package com.shoppingcart.controller;

import com.shoppingcart.entity.Order;
import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.ShippingAddressRepository;
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
    private OrderRepository orderRepository;

    @PostMapping("/checkout/{shippingAddressId}")
    public ResponseEntity<?> checkout(@PathVariable("shippingAddressId") Integer shippingAddressId,
                                      @RequestBody Order order){
        Optional<ShippingAddress> shippingAddressOptional = shippingAddressRepository.findById(shippingAddressId);
        if(!shippingAddressOptional.isPresent()){
            return new ResponseEntity<>("Shipping Address not found with id "+shippingAddressId,HttpStatus.NOT_FOUND);
        }
        	order.setOrderId(java.util.UUID.randomUUID().toString());
            orderRepository.save(order);
            return new ResponseEntity<>(order.getOrderId(), HttpStatus.OK);
    }
}
