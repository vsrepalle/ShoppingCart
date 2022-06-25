package com.shoppingcart.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider
                = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/product/add/**","/product/update/{productId}/**","/product/delete/{productId}/**",
                                        "/product/topRated/**","/account/findUsers/**","/account/makeAdmin/{accountId}/**",
                                        "/product/lowStockProduct/**","/account/getOrdersInFutureDate/**","/account/confirmOrder/{orderId}")
                .hasAuthority("ADMIN")
                .antMatchers("/users/{accountId}/cart/**","/products/**", "/wishlist/account/**",
                                        "/product/**","/orders/**","/checkout/{shippingAddressId}",
                                        "/shippingaddress/add","/search/**")
                .hasAuthority("USER")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/users/account","/account/login");
    }

}
