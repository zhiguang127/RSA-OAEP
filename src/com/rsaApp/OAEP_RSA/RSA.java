package com.rsaApp.OAEP_RSA;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class RSA {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static int Length = 1024;
    private static int messageLength = 1024;

    // 1024 bits 的 RSA 密钥对，最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;
    // 1024 bits 的 RSA 密钥对，最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    private final OAEP oaep;

    public void setMessageLength(int length) {
        messageLength = length;
    }

    public RSA() {
        this.oaep = new OAEP();
    }

    // 获取公钥
    public static PublicKey getPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    // 获取私钥
    public static PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        byte[] privateKeyByte = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }


    // 分段加密
    public String encrypt(String plainText, String publicKeyStr, String G) throws Exception {
        byte[] paddedMessage0 = this.oaep.Padding(plainText);
        int l = paddedMessage0.length;

        String padedMessage = Util.oaepToString(paddedMessage0);
        System.out.println("padedMessage: " + padedMessage);
        String oaepEncrypted = oaep.OaepEncrypt(padedMessage, G);
        System.out.println("oaepEncrypted: " + oaepEncrypted);

        byte[] plainTextArray = oaepEncrypted.getBytes(StandardCharsets.UTF_8);
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = plainTextArray.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK)
                cache = cipher.doFinal(plainTextArray, offSet, MAX_ENCRYPT_BLOCK);
            else
                cache = cipher.doFinal(plainTextArray, offSet, inputLen - offSet);
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptText = out.toByteArray();
        out.close();
        return Base64.getEncoder().encodeToString(encryptText);
    }

    // 分段解密
    public String decrypt(String encryptTextHex, String privateKeyStr, String G) throws Exception {
        byte[] encryptText = Base64.getDecoder().decode(encryptTextHex);
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptText, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptText, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] plainText = out.toByteArray();
        out.close();

        String rsaDecrypted = new String(plainText);
        System.out.println("rsaDecrypted: " + rsaDecrypted);
        String oaepDecrypted = this.oaep.OaepDecrypt(rsaDecrypted, G);
        System.out.println("oaepDecrypted: " + oaepDecrypted);


        return this.oaep.BinaryToString(oaepDecrypted);

    }

    //测试
    public static void main(String[] args) throws Exception {
        RSA rsa = new RSA();
        //从RSAKeyGenertor中获取公钥和私钥
        RSAKeyGenertor rsaKeyGenertor = new RSAKeyGenertor();
        Map<String, Object> keyMap = rsaKeyGenertor.initKey(1024);
        String publicKey = rsaKeyGenertor.getPublicKeyStr(keyMap);
        String privateKey = rsaKeyGenertor.getPrivateKeyStr(keyMap);

        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
        //把公钥和私钥写入文件
        //Util.writePublicKey(publicKey, "publicKey.pem");
        //Util.writePrivateKey(privateKey, "privateKey.pem");

        String message = "欲穷千里目，更上一层楼";
        int messageLength = message.length();
        System.out.println("message: " + message);
        String rsaEncrypted = rsa.encrypt(message, publicKey, "SHA256");
        System.out.println("rsaEncrypted: " + rsaEncrypted);

        String rsaDecrypted = rsa.decrypt(rsaEncrypted, privateKey, "SHA256");
        rsaDecrypted = rsaDecrypted.substring(0, messageLength);
        System.out.println("finaDecrypted: " + rsaDecrypted);
        //获取rsa的oaep对象的messageLength
        int l = rsa.oaep.getMessageLength();
        rsa.setMessageLength(l);
        System.out.println("messageLength: " + messageLength);

    }

}

