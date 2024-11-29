package com.nemuel.estoque.api.controller;

import com.nemuel.estoque.api.config.JwtUtil;
import com.nemuel.estoque.api.dto.AuthRequest;
import com.nemuel.estoque.api.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Gera o token JWT com username e roles
        String token = jwtUtil.generateToken(authentication.getName(), authentication.getAuthorities());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
