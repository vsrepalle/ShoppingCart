package com.shoppingcart.controller;

import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shippingaddress")
public class ShippingAddressController {
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addShippingAddress(ShippingAddress shippingAddress){
        shippingAddressRepository.save(shippingAddress);
        return new ResponseEntity<>("Shipping Address Created", HttpStatus.CREATED);
    }
}
