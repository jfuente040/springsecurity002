package com.jfuente040.springsecurity002.service;

import com.jfuente040.springsecurity002.dto.AuthResponse;
import com.jfuente040.springsecurity002.dto.LoginRequest;
import com.jfuente040.springsecurity002.dto.RegisterRequest;
import com.jfuente040.springsecurity002.persistence.entity.RoleEntity;
import com.jfuente040.springsecurity002.persistence.entity.RoleEnum;
import com.jfuente040.springsecurity002.persistence.entity.UserEntity;
import com.jfuente040.springsecurity002.persistence.repository.RoleRepository;
import com.jfuente040.springsecurity002.persistence.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Generar token JWT
            String token = jwtService.generateToken(authentication);

            return AuthResponse.builder()
                    .token(token)
                    .username(authentication.getName())
                    .message("Login exitoso")
                    .build();

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas", e);
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Buscar el rol USER
        RoleEntity userRole = roleRepository.findByRoleName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        // Crear nuevo usuario
        UserEntity user = UserEntity.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .roles(Set.of(userRole))
                .build();

        // Guardar usuario
        user = userRepository.save(user);

        // Crear Authentication para generar token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), 
                null
        );

        // Generar token JWT
        String token = jwtService.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .message("Registro exitoso")
                .build();
    }
}
