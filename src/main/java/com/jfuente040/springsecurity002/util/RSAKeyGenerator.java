package com.jfuente040.springsecurity002.util;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Component
public class RSAKeyGenerator {

    public void generateRSAKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Guardar clave privada
            savePrivateKeyToPEM(privateKey, "src/main/resources/certs/private_key.pem");
            
            // Guardar clave pública
            savePublicKeyToPEM(publicKey, "src/main/resources/certs/public_key.pem");

            System.out.println("✅ Claves RSA generadas exitosamente:");
            System.out.println("📁 Clave privada: src/main/resources/certs/private_key.pem");
            System.out.println("📁 Clave pública: src/main/resources/certs/public_key.pem");

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Error al generar claves RSA", e);
        }
    }

    private void savePrivateKeyToPEM(PrivateKey privateKey, String filename) throws IOException {
        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("-----BEGIN PRIVATE KEY-----\n");
            writer.write(insertLineBreaks(encoded, 64));
            writer.write("\n-----END PRIVATE KEY-----\n");
        }
    }

    private void savePublicKeyToPEM(PublicKey publicKey, String filename) throws IOException {
        String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("-----BEGIN PUBLIC KEY-----\n");
            writer.write(insertLineBreaks(encoded, 64));
            writer.write("\n-----END PUBLIC KEY-----\n");
        }
    }

    private String insertLineBreaks(String input, int lineLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i += lineLength) {
            result.append(input, i, Math.min(i + lineLength, input.length()));
            if (i + lineLength < input.length()) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
