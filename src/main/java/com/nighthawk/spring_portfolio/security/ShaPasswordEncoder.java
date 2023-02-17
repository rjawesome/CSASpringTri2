package com.nighthawk.spring_portfolio.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ShaPasswordEncoder implements PasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            ((String) rawPassword).getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            return computedPasswordHash;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
            ((String) rawPassword).getBytes(StandardCharsets.UTF_8));
            String computedPasswordHash = new String(encodedHash);
            return(computedPasswordHash.equals(encodedPassword));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
