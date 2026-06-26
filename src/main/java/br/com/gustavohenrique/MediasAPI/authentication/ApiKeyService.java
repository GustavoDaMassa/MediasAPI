package br.com.gustavohenrique.MediasAPI.authentication;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class ApiKeyService {

    private static final String PREFIX = "mapi_";
    private static final int RANDOM_BYTES = 32;

    public String generate() {
        byte[] bytes = new byte[RANDOM_BYTES];
        new SecureRandom().nextBytes(bytes);
        return PREFIX + toHex(bytes);
    }

    public String hash(String rawKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return toHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public String extractPrefix(String rawKey) {
        return rawKey.substring(0, Math.min(rawKey.length(), 13));
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
