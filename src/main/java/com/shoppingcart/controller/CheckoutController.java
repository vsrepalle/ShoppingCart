package com.shoppingcart.controller;

import com.shoppingcart.entity.Account;
import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
public class CheckoutController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @PostMapping("/checkout/{accountId}")
    public ResponseEntity<?> checkout(@RequestBody ShippingAddress shippingAddress
            ,@PathVariable("accountId") Integer accountId){
        Optional<Account> accountOptional=accountRepository.findById(accountId);
        if(accountOptional.isPresent()){
            if(!shippingAddress.getCardNumber().equals("^([0-9]{4}[- ]?){3}[0-9]{4}$")){
                return new ResponseEntity<>("Card Number is not valid",HttpStatus.BAD_REQUEST);
            }
            if(!(String.valueOf(shippingAddress.getCvv()).equals("^[0-9]{3, 4}$"))){
                return new ResponseEntity<>("Cvv is not valid",HttpStatus.BAD_REQUEST);
            }
            if(shippingAddress.getExpiryMonth() > 12){
                return new ResponseEntity<>("Expiry Month Is Greater Than 12",HttpStatus.BAD_REQUEST);
            }

            if(isCardExpired(shippingAddress)){
                return new ResponseEntity<>("Card is Expired",HttpStatus.EXPECTATION_FAILED);
            }
            shippingAddressRepository.save(shippingAddress);
        }
        return new ResponseEntity<>("Account Not Found With Id "+accountId, HttpStatus.NOT_FOUND);
    }
    private boolean isCardExpired(ShippingAddress shippingAddress){
        LocalDate date = LocalDate.now();
        if(shippingAddress.getExpiryYear()>date.getYear()){
            return false;
        }
        else if(shippingAddress.getExpiryYear()<date.getYear()){
            return true;
        }
        else{
            return shippingAddress.getExpiryMonth() <= date.getMonthValue() &&
                    shippingAddress.getExpiryMonth() != date.getMonthValue();
        }
    }
}
