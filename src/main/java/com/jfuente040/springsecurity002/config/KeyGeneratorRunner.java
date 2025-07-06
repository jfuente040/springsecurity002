package com.jfuente040.springsecurity002.config;

import com.jfuente040.springsecurity002.util.RSAKeyGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class KeyGeneratorRunner implements CommandLineRunner {

    private final RSAKeyGenerator rsaKeyGenerator;

    public KeyGeneratorRunner(RSAKeyGenerator rsaKeyGenerator) {
        this.rsaKeyGenerator = rsaKeyGenerator;
    }

    @Override
    public void run(String... args) throws Exception {
        File privateKeyFile = new File("src/main/resources/certs/private_key.pem");
        File publicKeyFile = new File("src/main/resources/certs/public_key.pem");

        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            System.out.println("🔑 Generando claves RSA...");
            rsaKeyGenerator.generateRSAKeys();
        } else {
            System.out.println("🔑 Claves RSA ya existen, usando las existentes.");
        }
    }
}
