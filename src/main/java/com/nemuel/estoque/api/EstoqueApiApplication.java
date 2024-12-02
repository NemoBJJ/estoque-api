package com.nemuel.estoque.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EstoqueApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstoqueApiApplication.class, args);
    }
}
