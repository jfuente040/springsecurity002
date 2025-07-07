package com.jfuente040.springsecurity002.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret:mySecretKey}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private Long jwtExpiration;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretKey);
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpiration);

        return JWT.create()
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(getAlgorithm());
    }

    public String extractUsername(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    public String extractAuthorities(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getClaim("authorities").asString();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            verifyToken(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }
}
