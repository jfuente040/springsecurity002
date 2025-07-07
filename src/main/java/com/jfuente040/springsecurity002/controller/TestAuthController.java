package com.jfuente040.springsecurity002.controller;

import com.jfuente040.springsecurity002.dto.AuthResponse;
import com.jfuente040.springsecurity002.dto.LoginRequest;
import com.jfuente040.springsecurity002.dto.RegisterRequest;
import com.jfuente040.springsecurity002.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TestAuthController {

    private final AuthService authService;

    public TestAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.builder()
                            .message("Error de autenticación: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.builder()
                            .message("Error en el registro: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from TestAuthController";
    }

    @GetMapping("/hello-secured")
    public String helloSecured(Authentication authentication) {
        return "Hello from TestAuthController secured. Usuario: "
         + authentication.getName() + 
               ", Authorities: " + authentication.getAuthorities();
    }

    @GetMapping("/others")
    public String others(Authentication authentication) {
        return "Hello from TestAuthController others. Usuario: "
         + authentication.getName();
    }




}
