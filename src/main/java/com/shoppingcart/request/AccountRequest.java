package com.shoppingcart.request;

import lombok.Data;

@Data
public class AccountRequest {
    private String name;
    private String password;
    private String confirmPassword;
    private String email;
    private String role;
}
