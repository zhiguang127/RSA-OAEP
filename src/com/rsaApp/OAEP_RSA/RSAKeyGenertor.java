package com.rsaApp.OAEP_RSA;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAKeyGenertor {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static int Length = 1024;

    public static Map<String, Object> initKey(int keysize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 设置密钥对的 bit 数，越大越安全
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 获取公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

        // 获取公钥字符串
    public static String getPublicKeyStr(Map<String, Object> keyMap) {
        // 获得 map 中的公钥对象，转为 key 对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        // 编码返回字符串
        return encryptBASE64(key.getEncoded());
    }


    // 获取私钥字符串
    public static String getPrivateKeyStr(Map<String, Object> keyMap) {
        // 获得 map 中的私钥对象，转为 key 对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        // 编码返回字符串
        return encryptBASE64(key.getEncoded());
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

    public static String encryptBASE64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }

}
