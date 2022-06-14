package com.shoppingcart.service.impl;

import com.shoppingcart.entity.Product;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.repository.CartRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private ProductRepository productRepository;

    private AccountRepository accountRepository;

    private CartRepository cartRepository;
    @BeforeEach
    void setUp () {
        shoppingCartService=new ShoppingCartServiceImpl(productRepository,cartRepository,accountRepository);
    }



    @Test
    @DisplayName("Get All Products")
    void getAllProducts() {
        Product product=new Product();
        product.setProdName("Laptop");
        product.setPrice(50000);
        product.setCategory("Electronics");
        product.setStockQty(1000);
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        List<Product> products= shoppingCartService.getAllProducts();
        assertEquals(products.get(0),product);
    }
}