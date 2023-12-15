package com.example.tp.security;

import java.security.PublicKey;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;

public class RSAEncryptor {

    private PublicKey publicKey;

    public RSAEncryptor(String publicKeyBase64) {
        this.publicKey = loadPublicKey(publicKeyBase64);
    }

    public byte[] encryptData(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            handleException("Error encrypting data", e);
            return null;
        }
    }

    private PublicKey loadPublicKey(String base64Key) {
        try {
            byte[] keyBytes = java.util.Base64.getDecoder().decode(base64Key);
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            handleException("Error loading public key", e);
            return null;
        }
    }

    private void handleException(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}