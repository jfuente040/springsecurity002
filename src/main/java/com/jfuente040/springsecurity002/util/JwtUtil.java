package com.jfuente040.springsecurity002.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jfuente040.springsecurity002.config.JwtProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Cargar las claves RSA al inicializar el componente
    private void loadKeys() {
        try {
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar las claves RSA", e);
        }
    }

    private RSAPrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource resource = new ClassPathResource(jwtProperties.getPrivateKeyPath());
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        // Limpiar el contenido de la clave privada
        // Eliminar encabezados y pies de página, y espacios en blanco
        content = content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        // Decodificar la clave privada desde Base64
        byte[] keyBytes = Base64.getDecoder().decode(content);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private RSAPublicKey loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        ClassPathResource resource = new ClassPathResource(jwtProperties.getPublicKeyPath());
        String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        content = content.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(content);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    // Generar token JWT
    public String createToken(Authentication authentication) {
        try {
            // Cargar claves si no están cargadas
            if (privateKey == null || publicKey == null) {
                loadKeys();
            }

            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

            String username = authentication.getPrincipal().toString();
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            String jwtToken = JWT.create()
                    .withIssuer(this.jwtProperties.getUserGenerator())
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getTokenExpirationTime()))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);

            return jwtToken;
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error al crear el token JWT", exception);
        }
    }

    // Validar token JWT
    public DecodedJWT validateToken(String token) {
        try {
            // Cargar claves si no están cargadas
            if (privateKey == null || publicKey == null) {
                loadKeys();
            }

            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.jwtProperties.getUserGenerator())
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token JWT inválido, no autorizado");
        }
    }

    // Extraer username del token
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    // Extraer claim específico del token
    public String extractClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName).asString();
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    // Extraer todos los authorities del token
    public String[] extractAuthorities(DecodedJWT decodedJWT) {
        String authorities = decodedJWT.getClaim("authorities").asString();
        return authorities != null ? authorities.split(",") : new String[0];
    }
}
