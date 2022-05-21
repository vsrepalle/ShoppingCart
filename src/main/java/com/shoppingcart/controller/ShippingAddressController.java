package com.shoppingcart.controller;

import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/shippingaddress")
public class ShippingAddressController {
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @PostMapping("/add")
    public ResponseEntity<?> addShippingAddress(@RequestBody ShippingAddress shippingAddress){
    	if (!(shippingAddress.getCardNumber()
        		.length()==16&&onlyDigits(shippingAddress.getCardNumber()))) {
            return new ResponseEntity<>("Card Number is not valid", HttpStatus.BAD_REQUEST);
        }
        if (!(String.valueOf(shippingAddress.getCvv()).length()==3&&onlyDigits(String.valueOf(shippingAddress.getCvv())))) {
            return new ResponseEntity<>("Cvv is not valid", HttpStatus.BAD_REQUEST);
        }
        if (shippingAddress.getExpiryMonth() > 12) {
            return new ResponseEntity<>("Expiry Month Is Greater Than 12", HttpStatus.BAD_REQUEST);
        }
        if (isCardExpired(shippingAddress)) {
            return new ResponseEntity<>("Card is Expired", HttpStatus.EXPECTATION_FAILED);
        }
        shippingAddressRepository.save(shippingAddress);
        return new ResponseEntity<>("Shipping Address Created", HttpStatus.CREATED);
    }
    private static boolean
    onlyDigits(String str)
    {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);  
        return m.matches();
    }
    private static boolean isCardExpired(ShippingAddress shippingAddress){
        LocalDate date = LocalDate.now();
        if(shippingAddress.getExpiryYear()>date.getYear()){
            return false;
        }
            return shippingAddress.getExpiryYear()<date.getYear() ||
                    shippingAddress.getExpiryMonth() <= date.getMonthValue() &&
                    shippingAddress.getExpiryMonth() != date.getMonthValue();
    }
}
