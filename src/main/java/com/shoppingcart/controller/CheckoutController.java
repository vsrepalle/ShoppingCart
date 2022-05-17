package com.shoppingcart.controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.dto.CheckoutDTO;
import com.shoppingcart.entity.ShippingAddress;
import com.shoppingcart.repository.ShippingAddressRepository;
import com.shoppingcart.utils.AccountUtil;

@RestController
public class CheckoutController {

 
    @Autowired
    private ShippingAddressRepository shippingAddressRepository;
    @Autowired
    private AccountUtil accountUtil;

    @PostMapping("/checkout/{accountId}/{shippingAddressId}")
    public ResponseEntity<?> checkout(@PathVariable("shippingAddressId") Integer shippingAddressId,
                                      @RequestBody CheckoutDTO checkoutDTO){
        Optional<ShippingAddress> shippingAddressOptional = shippingAddressRepository.findById(shippingAddressId);
        if(!shippingAddressOptional.isPresent()){
            return new ResponseEntity<>("Shipping Address not found with id "+shippingAddressId,HttpStatus.NOT_FOUND);
        }
        ResponseEntity<String> validateAccount = accountUtil.validateAccount(checkoutDTO.getAccount());
        if(validateAccount.getStatusCode().equals(HttpStatus.OK)) {
            ShippingAddress shippingAddress = shippingAddressOptional.get();
            if (shippingAddress.getCardNumber()
            		.length()==16&&onlyDigits(shippingAddress.getCardNumber())) {
                return new ResponseEntity<>("Card Number is not valid", HttpStatus.BAD_REQUEST);
            }
            if (String.valueOf(shippingAddress.getCvv()).length()==3&&onlyDigits(String.valueOf(shippingAddress.getCvv()))) {
                return new ResponseEntity<>("Cvv is not valid", HttpStatus.BAD_REQUEST);
            }
            if (shippingAddress.getExpiryMonth() > 12) {
                return new ResponseEntity<>("Expiry Month Is Greater Than 12", HttpStatus.BAD_REQUEST);
            }
            if (isCardExpired(shippingAddress)) {
                return new ResponseEntity<>("Card is Expired", HttpStatus.EXPECTATION_FAILED);
            }
            shippingAddressRepository.save(shippingAddress);
            checkoutDTO.getOrder().setOrderId(java.util.UUID.randomUUID().toString());
            return new ResponseEntity<>(checkoutDTO.getOrder().getOrderId(), HttpStatus.OK);
        }
        return validateAccount;
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
    public static boolean
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
}
