package com.outzone.main.tools;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncriptionManager {
    public static String encryptString(String str, String key){
        return str;
    }

    public static String decodeString(String str, String key){
        return str;
    }

    public static byte[] DigestSHA256(String input) {
        MessageDigest dig;
        try {
            dig = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return dig.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    //TODO: REVISAR
    /*public static String byteToString(byte[] hash){
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64){
            hexString.insert(0,'0');
        }
        return hexString.toString();
    }*/
    //Nueva función, parece evitar errores de acceso
    public static String byteToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getHashString(String str){
        return byteToString(DigestSHA256(str));
    }

    public static byte[] BlowFishEncrypt(String str, String key){
        byte[] encrypted;
        byte[] KeyData = key.getBytes();
        SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("Blowfish");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, KS);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        try {
            encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
        return encrypted;
    }

    public static byte[] BlowFishDecrypt(byte[] encrypted, String key){
        byte[] decrypted;
        byte[] KeyData = key.getBytes();
        SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("Blowfish");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, KS);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        try {
            decrypted = cipher.doFinal(encrypted);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
        return decrypted;
    }
}
