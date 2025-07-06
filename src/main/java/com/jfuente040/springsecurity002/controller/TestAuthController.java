package com.jfuente040.springsecurity002.controller;

import com.jfuente040.springsecurity002.controller.dto.AuthCreateUserRequest;
import com.jfuente040.springsecurity002.controller.dto.AuthLoginRequest;
import com.jfuente040.springsecurity002.controller.dto.AuthResponse;
import com.jfuente040.springsecurity002.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TestAuthController {

    private final AuthService authService;

    public TestAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.authService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.authService.createUser(userRequest), HttpStatus.CREATED);
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello from TestAuthController - Endpoint público";
    }

    @GetMapping("/hello-secured")
    public String helloSecured() {
        return "Hello from TestAuthController - Endpoint seguro (requiere autenticación)";
    }

    @GetMapping("/others")
    public String others() {
        return "Hello from TestAuthController - Otros endpoints";
    }
}
