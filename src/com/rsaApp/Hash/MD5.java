package com.rsaApp.Hash;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String enCode(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02x", b));
            }
            String sbin = sb.toString();
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < sbin.length(); i++) {
                String temp = sbin.substring(i, i + 1);
                int num = Integer.parseInt(temp, 16);
                String bin = Integer.toBinaryString(num);
                while (bin.length() < 4) {
                    bin = "0" + bin;
                }
                sb1.append(bin);
            }
            return sb1.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encrypt(String s){
         try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md5).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



}
