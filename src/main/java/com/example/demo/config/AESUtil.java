package com.example.demo.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class AESUtil {
    static Logger logger= LoggerFactory.getLogger(AESUtil.class);





    // Encrypt data using AES
    public static String encryptData(String data,String dynamicKey) throws Exception {

        SecretKey secretKey = new SecretKeySpec(dynamicKey.getBytes(), "AES");
        //logger.info("Secret key "+ Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt data using AES
    public static String decryptData(String encryptedData,String dynamicKey) throws Exception {

        SecretKey secretKey = new SecretKeySpec(dynamicKey.getBytes(StandardCharsets.UTF_8), "AES");
      //  logger.info("Secret key "+ Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
