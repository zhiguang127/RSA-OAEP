package com.rsaApp.OAEP_RSA;

//Util类：用于实现一些通用的功能，如Base64编码/解码，填充/取消填充等。

import java.nio.charset.StandardCharsets;

public class Util {
    //工具类的构造函数
    public Util() {
    }

    //将字节数组转换为二进制字符串
    public static String bytesToHexString(String hanzi) {
        byte[] utf8Bytes = hanzi.getBytes(StandardCharsets.UTF_8);
        StringBuilder binaryStrBuilder = new StringBuilder();
        for (byte b : utf8Bytes) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            binaryStrBuilder.append(binaryString);
        }
        return binaryStrBuilder.toString();
    }

    //将二进制字符串转换为UTF-8编码的字符串
    public static String hexStringToUTF8(String binaryStr) {
        int numBytes = binaryStr.length() / 8;
        byte[] bytes = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            String byteStr = binaryStr.substring(8 * i, 8 * (i + 1));
            bytes[i] = (byte) Integer.parseInt(byteStr, 2);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    //OAEP所需要的异或运算
    public static String XOR(String a, String b) {
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if ((int) a.charAt(i) == (int) b.charAt((i % 256))) {
                result += "0";
            } else {
                result += "1";
            }
        }
        return result;
    }

    //OAEP填充完之后所需要的oaepToString函数
    public static String oaepToString(byte[] paddedMessage) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < paddedMessage.length; i++) {
            str.append((int) paddedMessage[i]);
        }
        return str.toString();
    }


}
