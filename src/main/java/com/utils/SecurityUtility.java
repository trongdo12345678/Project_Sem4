package com.utils;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public final class SecurityUtility {

    public static String encryptBcrypt(String content) {
        try {
            return BCrypt.hashpw(content, BCrypt.gensalt(12));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password with Bcrypt", e);
        }
    }

    public static boolean compareBcrypt(String contentEncrypted, String content) {
        try {
            return BCrypt.checkpw(content, contentEncrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error comparing Bcrypt password", e);
        }
    }

    public static String encryptPBEPassword(String password) {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] enc = skf.generateSecret(spec).getEncoded();
            return contentCrypt(enc);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error encrypting password with PBKDF2", e);
        }
    }

    private static byte[] getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating salt", e);
        }
    }

    private static String contentCrypt(byte[] content) {
        BigInteger bi = new BigInteger(1, content);
        String hex = bi.toString(16);
        int paddingLength = (content.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}


