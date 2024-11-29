package com.nemuel.estoque.api.dto;

public class AuthResponse {
    private String token;

    // Construtor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }
}
