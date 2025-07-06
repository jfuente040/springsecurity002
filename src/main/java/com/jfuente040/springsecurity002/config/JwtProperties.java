package com.jfuente040.springsecurity002.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String userGenerator = "MyApp";
    private Long tokenExpirationTime = 86400000L; // 24 hours in milliseconds
    private String privateKeyPath = "certs/private_key.pem";
    private String publicKeyPath = "certs/public_key.pem";

    public String getUserGenerator() {
        return userGenerator;
    }

    public void setUserGenerator(String userGenerator) {
        this.userGenerator = userGenerator;
    }

    public Long getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(Long tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }
}
