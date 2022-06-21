package com.shoppingcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper mapper;


    @Test
    void getAllProducts() {

    }

    @Test
    @WithMockUser(username = "Admin1234@gmail.com",authorities = {"USER","ADMIN"})
    void addProduct() throws Exception {
        Product product = new Product();
        product.setProdName("Laptop");
        product.setCategory("Electronics");
        product.setStockQty(1000);
        product.setPrice(100000);
        String json = mapper.writeValueAsString(product);
        mockMvc.perform(post("/product/add").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void searchProductByCategory() {
    }

    @Test
    void topRated() {
    }

    @Test
    void rateProduct() {
    }

    @Test
    void getLowStockProduct() {
    }
}