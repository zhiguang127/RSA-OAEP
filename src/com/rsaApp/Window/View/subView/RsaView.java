package com.rsaApp.Window.View.subView;

import com.rsaApp.OAEP_RSA.RSA;
import com.rsaApp.OAEP_RSA.RSAKeyGenertor;
import com.rsaApp.OAEP_RSA.Util;
import com.rsaApp.shared.ComponentUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("serial")
public class RsaView extends JPanel {
    // 两个内部成员对象
    private final KeysPanel keys;
    private final CryptionPanel crypto;
    //一个外部成员对象
    private final RSA rsa = new RSA();
    private int messageLength = 0;


    private void setMessagelength(int length) {
        messageLength = length;
    }

    public RsaView() {
        setLayout(null);
        this.keys = new KeysPanel(this);
        this.crypto = new CryptionPanel(this);
        handleEvents();
    }

    //事件处理函数
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

        //当鼠标点击时，调用useGeneratedButton函数
        ComponentUtil.onMouseReleased(() -> crypto.cryptoInputText.setText(crypto.generatedText.getText()), crypto.useGeneratedButton);

        //当鼠标点击时，调用RsaEncrypt函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaEncrypt(keys.publicKeyText, keys.paddingList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "加密异常，请检查明文是否过长", "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }, crypto.encryptButton);

        //当鼠标点击时，调用RsaDecrypt函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                RsaDecrypt(keys.privateKeyText, keys.paddingList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "解密异常，请检查密文是否合法", "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
        }, crypto.decryptButton);

        //当鼠标点击时，调用PublicPemFile函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                PublicPemFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            applyErrorProtection();
        }, keys.loadPublicKeyButton);

        //当鼠标点击时，调用PrivatePemFile函数
        ComponentUtil.onMouseReleased(() -> {
            try {
                PrivatePemFile();
            } catch (Exception e) {
                // JOptionPane.showMessageDialog(null, "读写PEM文件异常" , "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
            applyErrorProtection();
        }, keys.loadPrivateKeyButton);
    }

    private void PrivatePemFile() throws IOException {
        String privateKeyPemfilePath = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("请选择文件或目录");
        fileChooser.showOpenDialog(null);
        String path = fileChooser.getSelectedFile().getPath();
        //如果选择的是文件，则直接写入或读取文件文件
        if (fileChooser.getSelectedFile().isFile()) {
            //如果公钥输入框不为空，则写入公钥文件
            privateKeyPemfilePath = path;
            if (!keys.privateKeyText.getText().isEmpty()) {
                Util.writePublicKey(keys.privateKeyText.getText(), privateKeyPemfilePath);
                JOptionPane.showMessageDialog(null, "写入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String publicKey = Util.readPublicKey(privateKeyPemfilePath);
                keys.privateKeyText.setText(publicKey);
                JOptionPane.showMessageDialog(null, "读取成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if (!keys.privateKeyText.getText().isEmpty()) {
                privateKeyPemfilePath = path + "/privateKey.pem";
                Util.writePublicKey(keys.privateKeyText.getText(), privateKeyPemfilePath);
                JOptionPane.showMessageDialog(null, "写入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void PublicPemFile() throws IOException {
        String publicKeyPemfilePath = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("请选择文件或目录");
        fileChooser.showOpenDialog(null);
        String path = fileChooser.getSelectedFile().getPath();
        //如果选择的是文件，则直接写入或读取文件文件
        if (fileChooser.getSelectedFile().isFile()) {
            //如果公钥输入框不为空，则写入公钥文件
            publicKeyPemfilePath = path;
            if (!keys.publicKeyText.getText().isEmpty()) {
                Util.writePublicKey(keys.publicKeyText.getText(), publicKeyPemfilePath);
                //弹出一个对话框，提示用户写入成功
                JOptionPane.showMessageDialog(null, "写入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String publicKey = Util.readPublicKey(publicKeyPemfilePath);
                keys.publicKeyText.setText(publicKey);
                //弹出一个对话框，提示用户读取成功
                JOptionPane.showMessageDialog(null, "读取成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            //如果选择的是目录，则在目录下生成文件再写入文件
            if (!keys.publicKeyText.getText().isEmpty()) {
                publicKeyPemfilePath = path + "/publicKey.pem";
                Util.writePublicKey(keys.publicKeyText.getText(), publicKeyPemfilePath);
                //弹出一个对话框，提示用户写入成功
                JOptionPane.showMessageDialog(null, "写入成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
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
        private final JButton loadPublicKeyButton;
        private final JButton loadPrivateKeyButton;

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
            generateButton.setBounds(333, 190, 96, 23);
            clearButton = new JButton("清空");
            clearButton.setBounds(441, 190, 96, 23);
            clearButton.setEnabled(false);
            loadPrivateKeyButton = new JButton("导入/出私钥");
            loadPrivateKeyButton.setBounds(225, 190, 96, 23);
            loadPublicKeyButton = new JButton("导入/出公钥");
            loadPublicKeyButton.setBounds(117, 190, 96, 23);


            JLabel paddingLabel = new JLabel("H&G函数:");
            paddingLabel.setBounds(10, 175, 66, 14);

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
            paddingList.setBounds(10, 190, 88, 21);
            paddingList.setSelectedIndex(1);
            ComponentUtil.add(this, privateKeyLabel, publicKeyLabel,
                    privateKeyScroller, publicKeyScroller,
                    generateButton, clearButton, loadPrivateKeyButton, loadPublicKeyButton,
                    paddingLabel, paddingList);

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

            JLabel cryptionInputLabel = new JLabel("Cypher / Plain Text:");
            cryptionInputLabel.setBounds(10, 24, 112, 14);
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

    //这个函数用于错误保护，不符合条件的按钮将被禁用
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
