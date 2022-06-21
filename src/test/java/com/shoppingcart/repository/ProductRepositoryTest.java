package com.shoppingcart.repository;

import com.shoppingcart.entity.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("Find By Product Name")
    @Order(3)
    void findByProductName() {
        Product product=new Product();
        product.setProdName("Laptop");
        product.setPrice(50000);
        product.setCategory("Electronics");
        product.setStockQty(1000);
        product = productRepository.save(product);
        List<Product> products = productRepository.findByProductName("L");
        assertTrue(product.equals(products.get(0)));
    }

    @Test
    @DisplayName("Find By Category")
    @Order(4)
    void findByCategory() {
    }

    @Test
    @DisplayName("Get Low Stock Product")
    @Order(5)
    void getLowStockProduct() {
    }
}