package com.rsaApp.OAEP_RSA;

//Util类：用于实现一些通用的功能，如Base64编码/解码，填充/取消填充等。

import java.io.*;
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
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            if ((int) a.charAt(i) == (int) b.charAt((i % 256))) {
                result.append("0");
            } else {
                result.append("1");
            }
        }
        return result.toString();
    }

    //OAEP填充完之后所需要的oaepToString函数
    public static String oaepToString(byte[] paddedMessage) {
        StringBuilder str = new StringBuilder();
        for (byte b : paddedMessage) {
            str.append((int) b);
        }
        return str.toString();
    }


    public static void writePrivateKey(String base64EncodedKey, String pemFilePath) throws IOException {
        // 添加PEM格式的起始和结束标记
        String pemData = "-----BEGIN PRIVATE KEY-----\n" + base64EncodedKey + "\n-----END PRIVATE KEY-----";
        writeToFile(pemData, pemFilePath);
    }

    public static void writePublicKey(String base64EncodedKey, String pemFilePath) throws IOException {
        // 添加PEM格式的起始和结束标记
        String pemData = "-----BEGIN PUBLIC KEY-----\n" + base64EncodedKey + "\n-----END PUBLIC KEY-----";
        writeToFile(pemData, pemFilePath);
    }

    private static void writeToFile(String data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
            System.out.println("PEM文件写入成功！");
        }
    }

    public static String readPrivateKey(String pemFilePath) throws IOException {
        String pemData = readFileAsString(pemFilePath);
        return extractKeyFromPEM(pemData);
    }

    public static String readPublicKey(String pemFilePath) throws IOException {
        String pemData = readFileAsString(pemFilePath);
        return extractKeyFromPEM(pemData);
    }

    private static String readFileAsString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String extractKeyFromPEM(String pemData) {
        // 移除PEM格式的起始和结束标记
        return pemData.replaceAll("-----BEGIN (PUBLIC|PRIVATE) KEY-----\n", "")
                .replaceAll("\n-----END (PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("\n", "");
    }

}
