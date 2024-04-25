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
    private final CryptionPanel cryption;
    private final RSA rsa = new RSA();

    public RsaView() {
        setLayout(null);
        this.keys = new KeysPanel(this);
        this.cryption = new CryptionPanel(this);
        handleEvents();
    }

    //事件处理函数，全都调用工具类
    private void handleEvents() {
        ComponentUtil.onKeyReleased(() -> applyErrorProtection(), keys.privateKeyText, keys.publicKeyText, cryption.cryptionInputText);
        ComponentUtil.onMouseReleased(() -> handleClearButton(), keys.clearButton);
        ComponentUtil.onMouseReleased(() -> handleGeneratedClearButton(), cryption.genClearButton);
        ComponentUtil.onMouseReleased(() -> handleInputClearButton(), cryption.clearButton);

        //当鼠标点击时，调用RSAKeyGenertor函数生成密钥对
        ComponentUtil.onMouseReleased(() -> {
            try {
                generateKeyPair();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, keys.generateButton);
        ComponentUtil.onMouseReleased(() -> cryption.cryptionInputText.setText(cryption.generatedText.getText()), cryption.useGeneratedButton);

        //ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaEncrypter(), keys.publicKeyText), cryption.encrypteButton);
        // ComponentUtil.onMouseReleased(() -> generateRsaResult(Backend.get().getRsaDecrypter(), keys.privateKeyText), cryption.decrypteButton);
        //当鼠标点击时，调用RSA中加密函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaEncry(keys.publicKeyText);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, cryption.encrypteButton);
        //当鼠标点击时，调用RSA中解密函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaDecry(keys.privateKeyText);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, cryption.decrypteButton);

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

    private void RsaEncry(JTextArea keyPane) throws Exception {
        String key = keyPane.getText();
        //直接调用RSA中的加密函数
        String message = cryption.cryptionInputText.getText();
        //调用RSA中的加密函数
        String rsaEncrypted = rsa.encrypt(message, key);
        cryption.generatedText.setText(rsaEncrypted);

        applyErrorProtection();
    }

    private void  RsaDecry(JTextArea KeyPane) throws Exception {
        String key = KeyPane.getText();
        System.out.println("key: " + key);
        //直接调用RSA中的解密函数
        String message = cryption.cryptionInputText.getText();
        RSA rsa = new RSA();
        //调用RSA中的解密函数
        String rsaDecrypted = rsa.decrypt(message, key);
        cryption.generatedText.setText(rsaDecrypted);

        applyErrorProtection();
    }

    //这个类用于密钥面板
    private static class KeysPanel extends JPanel {
        private final JLabel privateKeyLabel;
        private final JLabel publicKeyLabel;
        private final JLabel paddingLabel;
        private final JTextArea privateKeyText;
        private final JTextArea publicKeyText;
        private final JScrollPane privateKeyScroller;
        private final JScrollPane publicKeyScroller;
        private final JComboBox paddingList;
        private final JButton generateButton;
        private final JButton clearButton;

        public KeysPanel(RsaView parent) {

            //设置属性，这里属性是一个边框，边框的标题是Settings，边框的颜色是黑色，边框的位置是左对齐，边框的位置是上对齐
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Keys", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 11, 564, 224);
            setLayout(null);


            privateKeyLabel = new JLabel("Private Key:");
            privateKeyLabel.setBounds(10, 24, 71, 14);
            privateKeyText = new JTextArea();
            privateKeyText.setLineWrap(true);
            privateKeyText.setBounds(10, 43, 525, 45);
            privateKeyScroller = new JScrollPane(privateKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            privateKeyScroller.setBounds(10, 43, 525, 45);

            publicKeyLabel = new JLabel("Public Key:");
            publicKeyLabel.setBounds(10, 105, 71, 14);
            publicKeyText = new JTextArea();
            publicKeyText.setLineWrap(true);
            publicKeyText.setBounds(10, 124, 525, 45);
            publicKeyScroller = new JScrollPane(publicKeyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            publicKeyScroller.setBounds(10, 124, 525, 45);


            generateButton = new JButton("生成密钥对");
            generateButton.setBounds(253, 190, 136, 23);
            clearButton = new JButton("清空");
            clearButton.setBounds(399, 190, 136, 23);
            clearButton.setEnabled(false);

            paddingLabel = new JLabel("H函数:");
            paddingLabel.setBounds(10, 175, 46, 14);
            paddingList = new JComboBox<>();
            //for (CryptionPadding padding : Backend.get().getRsaPaddings())
            //    paddingList.addItem(padding);
            //paddingList.setBounds(10, 190, 233, 21);
            //paddingList.setSelectedIndex(1);

            ComponentUtil.add(this, privateKeyLabel, publicKeyLabel,
            		privateKeyScroller, publicKeyScroller, generateButton,
            		clearButton,paddingLabel);

            ComponentUtil.add(parent, this);
        }
    }

    //这个类用于加密面板
    private static class CryptionPanel extends JPanel {
        private final JLabel cryptionInputLabel;
        private final JLabel generatedLabel;
        private final JTextArea cryptionInputText;
        private final JTextArea generatedText;
        private final JScrollPane cryptionInputScroller;
        private final JScrollPane generatedScroller;
        private final JButton encrypteButton;
        private final JButton decrypteButton;
        private final JButton useGeneratedButton;
        private final JButton clearButton;
        private final JButton genClearButton;

        public CryptionPanel(RsaView parent) {

            setLayout(null);
            setName("");
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Encryption / Decryption", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 265, 564, 258);

            cryptionInputLabel = new JLabel("Cypher / Plain Text");
            cryptionInputLabel.setBounds(10, 24, 110, 14);
            cryptionInputText = new JTextArea();
            cryptionInputText.setLineWrap(true);
            cryptionInputText.setBounds(10, 43, 525, 45);
            cryptionInputScroller = new JScrollPane(cryptionInputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            cryptionInputScroller.setBounds(10, 43, 525, 45);

            generatedLabel = new JLabel("Generated:");
            generatedLabel.setBounds(10, 149, 71, 14);
            generatedText = new JTextArea();
            generatedText.setLineWrap(true);
            generatedText.setEditable(false);
            generatedText.setBounds(10, 168, 525, 45);
            generatedScroller = new JScrollPane(generatedText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            generatedScroller.setBounds(10, 168, 525, 45);


            encrypteButton = new JButton("加密");
            encrypteButton.setBounds(10, 96, 136, 23);
            encrypteButton.setEnabled(false);
            decrypteButton = new JButton("解密");
            decrypteButton.setBounds(160, 96, 136, 23);
            decrypteButton.setEnabled(false);
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
            		cryptionInputScroller, generatedScroller, encrypteButton,
            		decrypteButton, useGeneratedButton, genClearButton, clearButton);

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
        cryption.generatedText.setText("");
        applyErrorProtection();
    }

    //这个函数用于处理输入明文的清除按钮
    private void handleInputClearButton() {
        cryption.cryptionInputText.setText("");
        applyErrorProtection();
    }


    //这个函数用于处理错误保护
    private void applyErrorProtection() {
        boolean cryptionPossible = !keys.publicKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.encrypteButton.setEnabled(cryptionPossible);

        boolean decryptionPossible = !keys.privateKeyText.getText().isEmpty() && !cryption.cryptionInputText.getText().isEmpty();
        cryption.decrypteButton.setEnabled(decryptionPossible);

        boolean clearIsUseful = !keys.publicKeyText.getText().isEmpty() || !keys.privateKeyText.getText().isEmpty();
        keys.clearButton.setEnabled(clearIsUseful);

        boolean inputClearIsUseful = !cryption.cryptionInputText.getText().isEmpty();
        cryption.clearButton.setEnabled(inputClearIsUseful);

        boolean genClearIsUseful = !cryption.generatedText.getText().isEmpty();
        cryption.genClearButton.setEnabled(genClearIsUseful);

        boolean generatePossible = keys.publicKeyText.getText().isEmpty() && keys.privateKeyText.getText().isEmpty();
        keys.generateButton.setEnabled(generatePossible);

        boolean useGeneratedPossible = !cryption.generatedText.getText().isEmpty();
        cryption.useGeneratedButton.setEnabled(useGeneratedPossible);
    }


}
