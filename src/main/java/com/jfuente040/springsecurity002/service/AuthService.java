package com.jfuente040.springsecurity002.service;

import com.jfuente040.springsecurity002.controller.dto.AuthCreateUserRequest;
import com.jfuente040.springsecurity002.controller.dto.AuthLoginRequest;
import com.jfuente040.springsecurity002.controller.dto.AuthResponse;
import com.jfuente040.springsecurity002.persistence.entity.PermissionEntity;
import com.jfuente040.springsecurity002.persistence.entity.RoleEntity;
import com.jfuente040.springsecurity002.persistence.entity.UserEntity;
import com.jfuente040.springsecurity002.persistence.repository.RoleRepository;
import com.jfuente040.springsecurity002.persistence.repository.UserRepository;
import com.jfuente040.springsecurity002.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserDetailsServiceImpl userDetailsService, UserRepository userRepository,
                      RoleRepository roleRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();
        //Autenticar al usuario
        Authentication authentication = this.authenticate(username, password);
        //Almacenar la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Generar el token JWT a partir del objeto Authentication autenticado
        String accessToken = jwtUtil.createToken(authentication);
        return new AuthResponse(username, "Usuario logeado correctamente", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Usuario o contraseña inválidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest createUserRequest) {
        String username = createUserRequest.username();
        String password = createUserRequest.password();
        String roleName = createUserRequest.roleRequest().roleListName();

        RoleEntity roleEntity = roleRepository.findByRoleName(
            com.jfuente040.springsecurity002.persistence.entity.RoleEnum.valueOf(roleName))
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe: " + roleName));

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(username + "@example.com") // Email por defecto
                .roles(Set.of(roleEntity))
                .isEnabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        UserEntity userSaved = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()));
            if (role.getPermissions() != null) {
                authorities.addAll(role.getPermissions().stream()
                        .map(PermissionEntity::getPermissionName)
                        .map(Enum::name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()));
            }
        });

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userSaved.getUsername(), null, authorities));

        String accessToken = jwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication());
        return new AuthResponse(userSaved.getUsername(), "Usuario creado correctamente", accessToken, true);
    }
}
