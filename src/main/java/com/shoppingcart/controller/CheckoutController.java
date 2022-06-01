package com.shoppingcart.controller;

import com.shoppingcart.entity.Order;
import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.OrderRepository;
import com.shoppingcart.repository.ShippingAddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class CheckoutController {


    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/checkout/{shippingAddressId}")
    public ResponseEntity<?> checkout(@PathVariable("shippingAddressId") Integer shippingAddressId,
                                      @RequestBody Order order){
    	log.debug("checking the shipping address and card details",+shippingAddressId);
        Optional<ShippingAddress> shippingAddressOptional = shippingAddressRepository.findById(shippingAddressId);
        if(!shippingAddressOptional.isPresent()){
        	log.debug("Shipping Address is not found with id");
            return new ResponseEntity<>("Shipping Address not found with id "+shippingAddressId,HttpStatus.NOT_FOUND);
        }
        	order.setOrderId(java.util.UUID.randomUUID().toString());
            orderRepository.save(order);
            log.debug("Shipping Address sucessfully founded");
            return new ResponseEntity<>(order.getOrderId(), HttpStatus.OK);
    }
}
