package com.nemuel.estoque.api.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("admin")
                .password("$2a$10$7H9GgfIX04o/ciRtrvOJWeUbTzRc1kSmfV9ZzGPORlzPHRmJeDZFu") // senha: admin
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
