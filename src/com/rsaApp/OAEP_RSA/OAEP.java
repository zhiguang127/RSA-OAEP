package com.rsaApp.OAEP_RSA;

import com.rsaApp.Hash.MD4;
import com.rsaApp.Hash.MD5;
import com.rsaApp.Hash.SHA256;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

//这个类用来实现RSA的OAEP填充

public class OAEP {
    public int r_Length = 256; //r的长度，这里约定为256位
    public int pad_Length = 1024; //计算填充长度
    public int messageLength = 1024; //明文长度
    //用于存放256位的随机数
    public byte[] random = new byte[r_Length];

    public int getR_Length() {
        return r_Length;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setPad_Length(int pad_Length) {
        this.pad_Length = pad_Length;
    }

    public void setRandom(byte[] random) {
        this.random = random;
    }

    public byte[] getRandom() {
        return random;
    }

    //该函数用于生成一个256位的随机数
    public byte[] GenerateRandom(int length) throws UnsupportedEncodingException {
        byte[] random = new byte[length];
        for (int i = 0; i < length; i++) {
            int temp = new Random().nextInt(2);
            random[i] = (byte) temp;
        }
        setRandom(random);
        return random;
    }

    //对String类型的明文进行填充，填充规则是：在明文后面加上k1个0，然后加k0位的随机数，这里随机数的长度（k0）是256位
    public byte[] Padding(String message) throws UnsupportedEncodingException {
        //首先把明文进行UTF-8编码
        String message_U = String.valueOf(Util.bytesToHexString(message));
        // System.out.println("message_U: " + message_U);
        int pad_Length = RSA.Length - r_Length - 1 - message_U.length();
        setPad_Length(pad_Length);
        byte[] messageBytes = message_U.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = new byte[RSA.Length];
        int index = 0;
        for (char c : message_U.toCharArray()) {
            if (Character.isDigit(c)) {
                bytes[index++] = (byte) (c - '0');
            }
        }
        //保留处理成 01 序列后的明文长度
        setMessageLength(messageBytes.length);
        byte[] random = GenerateRandom(r_Length);
        byte[] paddedMessage = new byte[RSA.Length];
        for (int i = 0; i < this.getMessageLength(); i++) {
            paddedMessage[i] = bytes[i];
        }
        for (int i = this.getMessageLength(); i < this.getMessageLength() + pad_Length; i++) {
            paddedMessage[i] = 0;
        }
        for (int i = this.getMessageLength() + pad_Length; i < this.getMessageLength() + pad_Length + r_Length; i++) {
            paddedMessage[i] = random[i - messageBytes.length - pad_Length];
        }
        return paddedMessage;
    }

    //首先是Oaep的Encrypt，H和G可以是MD4，SHA256，MD5
    public String OaepEncrypt(String message, String G) throws UnsupportedEncodingException {

        String r = message.substring(RSA.Length - r_Length);
        String message_H = message.substring(0, RSA.Length - r_Length);

        //根据H的值的不同，选择不同的H运算，采用switch-case语句
        //其中H的值可能为“MD4”，“SHA256”，“MD5”
        switch (G) {
            case "MD4":
                //对r进行G运算
                String r_G_MD4 = G_MD4(r);
                r_G_MD4 = r_G_MD4 + r_G_MD4; //这里是为了让r_G_MD4的长度变为256位
                // System.out.println("r_G_MD4: " + r_G_MD4 + "length:" + r_G_MD4.length());
                String X_MD4 = Util.XOR(message_H, r_G_MD4);
                //对前面Length-k0位进行H运算
                String m_H_MD4 = H_MD4(X_MD4);
                m_H_MD4 = m_H_MD4 + m_H_MD4; //这里是为了让m_H_MD4的长度变为256位
                //对r和m_H_MD4进行异或运算
                String Y_MD4 = Util.XOR(m_H_MD4, r);
                return X_MD4 + Y_MD4;
            case "MD5":
                //对r进行G运算
                String r_G_MD5 = G_MD5(r);
                r_G_MD5 = r_G_MD5 + r_G_MD5; //这里是为了让r_G_MD5的长度变为256位
                String X_MD5 = Util.XOR(message_H, r_G_MD5);
                //对前面Length-k0位进行H运算
                String m_H_MD5 = H_MD5(X_MD5);
                m_H_MD5 = m_H_MD5 + m_H_MD5; //这里是为了让m_H_MD5的长度变为256位
                //对r_H和H(message_H)进行异或运算
                String Y_MD5 = Util.XOR(r, m_H_MD5);
                return X_MD5 + Y_MD5;
            case "SHA256":
                //对r进行G运算
                String r_G_SHA256 = G_SHA256(r);
                String X_SHA256 = Util.XOR(message_H, r_G_SHA256);
                //对前面Length-k0位进行H运算
                String m_H_SHA256 = H_SHA256(X_SHA256);
                //对r_H和H(message_H)进行异或运算
                String Y_SHA256 = Util.XOR(r, m_H_SHA256);
                return X_SHA256 + Y_SHA256;
            default:
                return null;
        }
    }

    //OAEP解密函数，结构与加密类似
    public String OaepDecrypt(String message, String G) throws UnsupportedEncodingException {
        String X = message.substring(0, RSA.Length - r_Length);
        String Y = message.substring(RSA.Length - r_Length);
        System.out.println(X.length());
        System.out.println(Y.length());
        //根据H的值的不同，选择不同的H运算
        //其中H的值可能为“MD4”，“SHA256”，“MD5”
        switch (G) {
            case "MD4":
                //对X进行H运算
                String X_H_MD4 = H_MD4(X);
                X_H_MD4 = X_H_MD4 + X_H_MD4; //这里是为了让X_H_MD4的长度变为256位
                // Y和X_H_MD4进行异或运算得到r
                String r1 = Util.XOR(Y, X_H_MD4);
                //对r进行G运算
                String r_G_MD4 = G_MD4(r1);
                r_G_MD4 = r_G_MD4 + r_G_MD4; //这里是为了让r_G_MD4的长度变为256位
                //对X与r_G_MD4进行异或运算得到message_H
                return Util.XOR(X, r_G_MD4);
            case "MD5":
                //对X进行H运算
                String X_H_MD5 = H_MD5(X);
                X_H_MD5 = X_H_MD5 + X_H_MD5; //这里是为了让X_H_MD5的长度变为256位
                // Y和X_H_MD5进行异或运算得到r
                String r2 = Util.XOR(Y, X_H_MD5);
                //对r进行G运算
                String r_G_MD5 = G_MD5(r2);
                r_G_MD5 = r_G_MD5 + r_G_MD5; //这里是为了让r_G_MD5的长度变为256位
                //使用X与r_G_MD5进行异或运算得到message_H
                return Util.XOR(X, r_G_MD5);
            case "SHA256":
                //对X进行H运算
                String X_H_SHA256 = H_SHA256(X);
                // Y和X_H_SHA256进行异或运算得到r
                String r3 = Util.XOR(Y, X_H_SHA256);
                //对r进行G运算
                String r_G_SHA256 = G_SHA256(r3);
                //使用X与r_G_SHA256进行异或运算得到message_H
                return Util.XOR(X, r_G_SHA256);
            default:
                return null;

        }
    }

    //把二进制序列转换为字符串
    public String BinaryToString(String binary) {
        //调用Util类中的函数，将二进制字符串转换为十六进制字符串并求出其UTF-8编码的字符串
        return Util.hexStringToUTF8(binary);
    }

    //此段是哈希函数的调用部分
    public String G_MD4(String random_H) throws UnsupportedEncodingException {
        return MD4.enCode(random_H);
    }

    public String G_SHA256(String random_H) throws UnsupportedEncodingException {
        return SHA256.enCode(random_H);
    }

    public String G_MD5(String random_H) throws UnsupportedEncodingException {
        return MD5.enCode(random_H);
    }

    public String H_MD4(String message) {
        String message_H = message.substring(0, RSA.Length - r_Length);
        return MD4.enCode(message_H);
    }

    public String H_SHA256(String message) {
        String message_H = message.substring(0, RSA.Length - r_Length);
        return SHA256.enCode(message_H);
    }

    public String H_MD5(String message) {
        String message_H = message.substring(0, RSA.Length - r_Length);
        return MD5.enCode(message_H);
    }

    //构造函数
    public OAEP() {
    }

    //  测试一下
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        OAEP oaep = new OAEP();
//        String message = "Hello, World!";
//        byte[] paddedMessage = oaep.Padding(message);
//        int l = paddedMessage.length;
//        System.out.println("l: " + l);
//
//        // 将填充后的明文每一位都转成数字打印出来并写入到一个字符串中
//        StringBuilder str = new StringBuilder();
//        for (int i = 0; i < l; i++) {
//            str.append((int) paddedMessage[i]);
//        }
//        String str1 = str.toString();
////      System.out.println(" ");
//        System.out.println("Padded Message: " + str1);
//        System.out.println("Padded Message Length: " + str1.length());
//
//        //接下来调用Encrypt函数，对填充后的明文进行加密
//        System.out.print("\n");
//        System.out.println("start1");
//        String result = oaep.OaepEncrypt(str1, "SHA256");
//        System.out.println("OAEP Result: " + result);
//        System.out.println("OAEP Result Length: " + result.length());
//
//        //然后调用Decrypt函数，对加密后的密文进行解密
//        System.out.print("\n");
//        System.out.println("start2");
//        String result1 = oaep.OaepDecrypt(result, "SHA256");
//        System.out.println("Decry Result: " + result1);
//        System.out.println("Decry Result Length: " + result1.length());
//
//        //截取明文长度的二进制串
//        String result2 = result1.substring(0, oaep.getMessageLength());
//        //最后把解密后的二进制序列转换为字符串
//        System.out.print("\n");
//        System.out.println("start3");
//        String result3 = oaep.BinaryToString(result2);
//        System.out.println("Decry Result: " + result3);
//        System.out.println("Decry Result Length: " + result3.length());
//
//    }


}
