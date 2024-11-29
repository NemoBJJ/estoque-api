package com.nemuel.estoque.api.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Configura BCrypt como o codificador de senha padrão
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Configura um usuário em memória com o papel ROLE_USER
        UserDetails user = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123")) // Senha codificada
                .roles("USER") // Isso será tratado como "ROLE_USER"
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
