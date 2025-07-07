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

    @GetMapping("/token-info")
    public ResponseEntity<Object> getTokenInfo(
            @RequestHeader("Authorization") String authHeader,
            Authentication authentication) {
        try {
            // Extraer el token del header Authorization
            String token = authHeader.substring(7); // Remover "Bearer "
            
            // Usar JwtService para extraer información del token
            String username = authService.getJwtService().extractUsername(token);
            String authorities = authService.getJwtService().extractAuthorities(token);
            String userGenerator = authService.getJwtService().extractUserGenerator(token);
            
            // Crear respuesta con información del token
            var tokenInfo = new java.util.HashMap<String, Object>();
            tokenInfo.put("username", username);
            tokenInfo.put("authorities", authorities);
            tokenInfo.put("userGenerator", userGenerator);
            tokenInfo.put("authenticatedUser", authentication.getName());
            tokenInfo.put("isValid", authService.getJwtService().isTokenValid(token));
            tokenInfo.put("isExpired", authService.getJwtService().isTokenExpired(token));
            
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al procesar el token: " + e.getMessage());
        }
    }




}
