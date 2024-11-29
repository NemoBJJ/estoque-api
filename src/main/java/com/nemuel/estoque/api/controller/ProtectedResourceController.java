package com.nemuel.estoque.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProtectedResourceController {

    @GetMapping("/protected-resource")
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("Acesso Permitido!");
    }
}
