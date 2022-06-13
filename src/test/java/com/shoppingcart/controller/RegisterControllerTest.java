package com.shoppingcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.entity.Account;
import com.shoppingcart.repository.AccountRepository;
import com.shoppingcart.request.AccountRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(com.shoppingcart.controller.RegisterController.class)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void register() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Nani");
        accountRequest.setRole("USER");
        accountRequest.setEmail("nani@gmail.com");
        accountRequest.setPassword("Nani1234@");
        accountRequest.setConfirmPassword("Nani1234@");
        when(accountRepository.save(accountArgumentCaptor.capture())).thenReturn(null);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(accountRequest);
        mockMvc.perform(post("/users/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Account Registered"));
    }
}