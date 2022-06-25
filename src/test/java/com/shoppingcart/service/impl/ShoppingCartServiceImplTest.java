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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(MockitoJUnitRunner.Silent.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private AccountRepository accountRepository;
    @Mock
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
      //  when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        doReturn(Collections.singletonList(product)).when(productRepository).findAll();
        List<Product> products= shoppingCartService.getAllProducts();
        assertEquals(products.get(0),product);
    }

    @Test
    @DisplayName("Check Product")
    void checkProduct(){
        Product product=new Product();
        product.setProdName("Laptop");
        product.setPrice(50000);
        product.setCategory("Electronics");
        product.setStockQty(1000);
       // when(productRepository.findById(idCaptor.capture())).thenReturn(Optional.ofNullable(product));
        doReturn(Optional.of(product)).when(productRepository).findById(ArgumentMatchers.any(Integer.class));
        assertDoesNotThrow(() -> {
            shoppingCartService.checkProduct(28);
        });
    }
}