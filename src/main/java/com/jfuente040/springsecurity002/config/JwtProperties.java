package com.jfuente040.springsecurity002.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Clave secreta para firmar los tokens JWT
     */
    private String secret = "mySecretKey";
    /**
     * Tiempo de expiración del token en milisegundos
     * Por defecto: 86400000ms = 24 horas
     */
    private Long expiration = 86400000L;
    /**
     * Usuario que genera los tokens JWT
     */
    private String userGenerator = "spring-security-app";

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public Long getExpiration() {
        return expiration;
    }
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
    public String getUserGenerator() {
        return userGenerator;
    }
    public void setUserGenerator(String userGenerator) {
        this.userGenerator = userGenerator;
    }
}
