package com.rsaApp.Window.View.subView;

import com.rsaApp.OAEP_RSA.RSA;
import com.rsaApp.OAEP_RSA.RSAKeyGenertor;
import com.rsaApp.shared.ComponentUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Map;

// @SuppressWarnings("serial")
public class RsaView extends JPanel {
    // 两个内部成员对象
    private final KeysPanel keys;
    private final CryptionPanel crypto;
    //一个外部成员对象
    private final RSA rsa = new RSA();
    private int messageLength = 0;


    private void setMessagelength(int length){
        messageLength = length;
    }

    public RsaView() {
        setLayout(null);
        this.keys = new KeysPanel(this);
        this.crypto = new CryptionPanel(this);
        handleEvents();
    }

    //事件处理函数，全都调用工具类
    private void handleEvents() {
        ComponentUtil.onKeyReleased(this::applyErrorProtection, keys.privateKeyText, keys.publicKeyText, crypto.cryptoInputText);
        ComponentUtil.onMouseReleased(this::handleClearButton, keys.clearButton);
        ComponentUtil.onMouseReleased(this::handleGeneratedClearButton, crypto.genClearButton);
        ComponentUtil.onMouseReleased(this::handleInputClearButton, crypto.clearButton);

        //当鼠标点击时，调用RSAKeyGenertor函数生成密钥对
        ComponentUtil.onMouseReleased(() -> {
            try {
                generateKeyPair();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "密钥生成异常: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }, keys.generateButton);
        ComponentUtil.onMouseReleased(() -> crypto.cryptoInputText.setText(crypto.generatedText.getText()), crypto.useGeneratedButton);

        //当鼠标点击时，调用RSA中加密函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaEncrypt(keys.publicKeyText, keys.paddingList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "加密异常 " , "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }, crypto.encryptButton);
        //当鼠标点击时，调用RSA中解密函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaDecrypt(keys.privateKeyText, keys.paddingList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "解密异常，请检查密文是否合法"  , "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }, crypto.decryptButton);

    }

    private void generateKeyPair() throws Exception {
        RSAKeyGenertor rsaKeyGenertor = new RSAKeyGenertor();
        Map<String, Object> keyMap = rsaKeyGenertor.initKey(1024);
        String publicKey = rsaKeyGenertor.getPublicKeyStr(keyMap);
        String privateKey = rsaKeyGenertor.getPrivateKeyStr(keyMap);
        keys.publicKeyText.setText(publicKey);
        keys.privateKeyText.setText(privateKey);
        applyErrorProtection();
    }

    private void RsaEncrypt(JTextArea keyPane, JComboBox paddings) throws Exception {
        String key = keyPane.getText();
        String G = paddings.getSelectedItem().toString();
        System.out.println("PublicKey: " + key);
        System.out.println("G: " + G);
        //直接调用RSA中的加密函数
        String message = crypto.cryptoInputText.getText();
        //设置messageLength
        setMessagelength(message.length());
        //调用RSA中的加密函数
        String rsaEncrypted = rsa.encrypt(message, key, G);
        crypto.generatedText.setText(rsaEncrypted);

        applyErrorProtection();
    }

    private void RsaDecrypt(JTextArea KeyPane, JComboBox paddinglist) throws Exception {
        String key = KeyPane.getText();
        String G = paddinglist.getSelectedItem().toString();
        System.out.println("PrivateKey: " + key);
        System.out.println("G: " + G);
        //直接调用RSA中的解密函数
        String message = crypto.cryptoInputText.getText();
        RSA rsa = new RSA();
        //调用RSA中的解密函数
        String rsaDecrypted = rsa.decrypt(message, key, G);
        //截取messageLength长度的字符串
        String result0 = rsaDecrypted.substring(0, messageLength);
        crypto.generatedText.setText(result0);

        applyErrorProtection();
    }

    //这个类用于密钥面板
    private static class KeysPanel extends JPanel {
        private final JTextArea privateKeyText;
        private final JTextArea publicKeyText;
        private final JComboBox paddingList;
        private final JButton generateButton;
        private final JButton clearButton;

        public KeysPanel(RsaView parent) {
            //设置边框
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Keys", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 11, 564, 224);
            setLayout(null);

            JLabel privateKeyLabel = new JLabel("Private Key:");
            privateKeyLabel.setBounds(10, 24, 71, 14);
            privateKeyText = new JTextArea();
            privateKeyText.setLineWrap(true);
            privateKeyText.setBounds(10, 43, 525, 45);
            JScrollPane privateKeyScroller = new JScrollPane(privateKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            privateKeyScroller.setBounds(10, 43, 525, 45);

            JLabel publicKeyLabel = new JLabel("Public Key:");
            publicKeyLabel.setBounds(10, 105, 71, 14);
            publicKeyText = new JTextArea();
            publicKeyText.setLineWrap(true);
            publicKeyText.setBounds(10, 124, 525, 45);
            JScrollPane publicKeyScroller = new JScrollPane(publicKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            publicKeyScroller.setBounds(10, 124, 525, 45);

            generateButton = new JButton("生成密钥对");
            generateButton.setBounds(253, 190, 136, 23);
            clearButton = new JButton("清空");
            clearButton.setBounds(399, 190, 136, 23);
            clearButton.setEnabled(false);

            JLabel paddingLabel = new JLabel("H函数:");
            paddingLabel.setBounds(10, 175, 46, 14);

            paddingList = new JComboBox();
            //把H函数添加到下拉框中
            String[] paddings = {"MD4", "MD5", "SHA256"};
            for (String padding : paddings) {
                try {
                    paddingList.addItem(padding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            paddingList.setBounds(10, 190, 233, 21);
            paddingList.setSelectedIndex(1);
            ComponentUtil.add(this, privateKeyLabel, publicKeyLabel,
                    privateKeyScroller, publicKeyScroller, generateButton,
                    clearButton, paddingLabel, paddingList);

            ComponentUtil.add(parent, this);
        }
    }

    //这个类用于加密面板
    private static class CryptionPanel extends JPanel {
        private final JTextArea cryptoInputText;
        private final JTextArea generatedText;
        private final JButton encryptButton;
        private final JButton decryptButton;
        private final JButton useGeneratedButton;
        private final JButton clearButton;
        private final JButton genClearButton;

        public CryptionPanel(RsaView parent) {

            setLayout(null);
            setName("");
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Encryption / Decryption", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 265, 564, 258);

            JLabel cryptionInputLabel = new JLabel("Cypher / Plain Text");
            cryptionInputLabel.setBounds(10, 24, 110, 14);
            cryptoInputText = new JTextArea();
            cryptoInputText.setLineWrap(true);
            cryptoInputText.setBounds(10, 43, 525, 45);
            JScrollPane cryptionInputScroller = new JScrollPane(cryptoInputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            cryptionInputScroller.setBounds(10, 43, 525, 45);

            JLabel generatedLabel = new JLabel("Generated:");
            generatedLabel.setBounds(10, 149, 71, 14);
            generatedText = new JTextArea();
            generatedText.setLineWrap(true);
            generatedText.setEditable(false);
            generatedText.setBounds(10, 168, 525, 45);
            JScrollPane generatedScroller = new JScrollPane(generatedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            generatedScroller.setBounds(10, 168, 525, 45);

            encryptButton = new JButton("加密");
            encryptButton.setBounds(10, 96, 136, 23);
            encryptButton.setEnabled(false);
            decryptButton = new JButton("解密");
            decryptButton.setBounds(160, 96, 136, 23);
            decryptButton.setEnabled(false);
            useGeneratedButton = new JButton("交换明/密文");
            useGeneratedButton.setBounds(10, 223, 136, 23);
            useGeneratedButton.setEnabled(false);
            genClearButton = new JButton("清空");
            genClearButton.setBounds(399, 223, 136, 23);
            genClearButton.setEnabled(false);
            clearButton = new JButton("清空");
            clearButton.setBounds(399, 96, 136, 23);
            clearButton.setEnabled(false);

            ComponentUtil.add(this, cryptionInputLabel, generatedLabel,
                    cryptionInputScroller, generatedScroller, encryptButton,
                    decryptButton, useGeneratedButton, genClearButton, clearButton);

            ComponentUtil.add(parent, this);
        }
    }

    //这个函数用于处理密钥面板的清除按钮
    private void handleClearButton() {
        keys.publicKeyText.setText("");
        keys.privateKeyText.setText("");
        keys.clearButton.setEnabled(false);
        applyErrorProtection();
    }

    //这个函数用于处理生成密文清除按钮
    private void handleGeneratedClearButton() {
        crypto.generatedText.setText("");
        applyErrorProtection();
    }

    //这个函数用于处理输入明文的清除按钮
    private void handleInputClearButton() {
        crypto.cryptoInputText.setText("");
        applyErrorProtection();
    }

    //这个函数用于处理错误保护
    private void applyErrorProtection() {
        boolean cryptoPossible = !keys.publicKeyText.getText().isEmpty() && !crypto.cryptoInputText.getText().isEmpty();
        crypto.encryptButton.setEnabled(cryptoPossible);

        boolean decryptionPossible = !keys.privateKeyText.getText().isEmpty() && !crypto.cryptoInputText.getText().isEmpty();
        crypto.decryptButton.setEnabled(decryptionPossible);

        boolean clearIsUseful = !keys.publicKeyText.getText().isEmpty() || !keys.privateKeyText.getText().isEmpty();
        keys.clearButton.setEnabled(clearIsUseful);

        boolean inputClearIsUseful = !crypto.cryptoInputText.getText().isEmpty();
        crypto.clearButton.setEnabled(inputClearIsUseful);

        boolean genClearIsUseful = !crypto.generatedText.getText().isEmpty();
        crypto.genClearButton.setEnabled(genClearIsUseful);

        boolean generatePossible = keys.publicKeyText.getText().isEmpty() && keys.privateKeyText.getText().isEmpty();
        keys.generateButton.setEnabled(generatePossible);

        boolean useGeneratedPossible = !crypto.generatedText.getText().isEmpty();
        crypto.useGeneratedButton.setEnabled(useGeneratedPossible);
    }

}
