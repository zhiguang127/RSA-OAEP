package com.rsaApp.Window.View.subView;

import com.rsaApp.shared.ComponentUtil;
import com.rsaApp.shared.ClipboardTUtil;
import com.rsaApp.Hash.MD4;
import com.rsaApp.Hash.MD5;
import com.rsaApp.Hash.SHA256;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HashView extends JPanel {
    private final HashView.messagePanel message;
    private final HashView.generatedPanel generated;

    public HashView() {
        setLayout(null);
        this.message = new HashView.messagePanel(this);
        this.generated = new HashView.generatedPanel(this);
        // ComponentUtil.add(this, message, generated);
        handleEvents();
    }

    //事件处理函数，全都调用工具类
    private void handleEvents() {
        ComponentUtil.onKeyReleased(this::applyErrorProtection, message.MessageText, generated.HashCodeText);
        ComponentUtil.onMouseReleased(this::handleClearButton, message.clearButton);
        ComponentUtil.onMouseReleased(this::handleGeneratedClearButton, generated.outputClearButton);
        ComponentUtil.onMouseReleased(() -> {
            try {
                HashEncrypt(message.MessageText, message.hashList, generated.HashCodeText, message.saltList, message.iterationList);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, message.generateButton);
        ComponentUtil.onMouseReleased(this::handleCopyButton, generated.copyButton);
    }

    private void HashEncrypt(JTextArea messagePane, JComboBox hashWay, JTextArea HashCode, JComboBox saltWay,JComboBox iterationWay) throws Exception {
        String message = messagePane.getText();
        if (message.isEmpty()) {
            //弹窗提示未输入明文
            JOptionPane.showMessageDialog(null, "未输入明文", "错误", JOptionPane.ERROR_MESSAGE);
            throw new Exception("未输入明文");
        }
        //System.out.println(message);
        String hashWayString = (String) hashWay.getSelectedItem();
        String saltWayString = (String) saltWay.getSelectedItem();
        int iteration = Integer.parseInt((String) iterationWay.getSelectedItem());
        // System.out.println(iteration);
        // System.out.println(saltWayString);
        String salt = null; //根据选择的盐值，返回对应的盐值
        if (saltWayString != null) {
            salt = ComponentUtil.getSalt(saltWayString);
        }
        // System.out.println(salt);
        //根据hashWayString的内容调用不同的哈希函数，switch语句
        if (hashWayString != null) {
            switch (hashWayString) {
                case "MD4":
                    HashCode.setText(MD4.encryptWithSalt(message, salt, iteration));
                    applyErrorProtection();
                    break;
                case "MD5":
                    HashCode.setText(MD5.encryptWithSalt(message, salt, iteration));
                    applyErrorProtection();
                    break;
                case "SHA256":
                    HashCode.setText(SHA256.encryptWithSalt(message, salt, iteration));
                    applyErrorProtection();
                    break;
                default:
                    throw new Exception("未知的哈希函数");
            }
        }
    }

    private void handleCopyButton() {
        ClipboardTUtil.setSysClipboardText(generated.HashCodeText.getText());
        //弹窗提示已写入剪切板
        JOptionPane.showMessageDialog(null, "已复制到剪切板", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    //这个类用于生成的Hash面板
    private static class messagePanel extends JPanel {
        private final JTextArea MessageText;
        private final JComboBox hashList;
        private final JComboBox saltList;
        private final JComboBox iterationList;
        private final JButton generateButton;
        private final JButton clearButton;

        public messagePanel(HashView parent) {

            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Input", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 11, 564, 250);
            setLayout(null);

            JLabel privateKeyLabel = new JLabel("Message:");
            privateKeyLabel.setBounds(10, 24, 71, 14);
            MessageText = new JTextArea();
            MessageText.setLineWrap(true);
            MessageText.setBounds(10, 43, 525, 45);
            JScrollPane privateKeyScroller = new JScrollPane(MessageText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            privateKeyScroller.setBounds(10, 43, 525, 151);

            generateButton = new JButton("加密");
            generateButton.setBounds(263, 219, 126, 23);
            clearButton = new JButton("清空");
            clearButton.setBounds(399, 219, 126, 23);
            clearButton.setEnabled(false);

            JLabel paddingLabel = new JLabel("哈希函数:");
            paddingLabel.setBounds(10, 203, 66, 14);

            hashList = new JComboBox();
            //把H函数添加到下拉框中
            String[] paddings = {"MD4", "MD5", "SHA256"};
            for (String padding : paddings) {
                try {
                    hashList.addItem(padding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hashList.setBounds(10, 219, 78, 21);
            hashList.setSelectedIndex(1);

            JLabel saltLabel = new JLabel("盐值:");
            saltLabel.setBounds(98, 203, 66, 14);

            saltList = new JComboBox();
            //把盐值添加到下拉框中
            String[] salts = {"无", "盐值1","盐值2","盐值3"};
            for (String salt : salts) {
                try {
                    saltList.addItem(salt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            saltList.setBounds(98, 219, 68, 21);
            saltList.setSelectedIndex(0);

            JLabel iterationLabel = new JLabel("迭代次数:");
            iterationLabel.setBounds(176, 203, 66, 14);

            iterationList = new JComboBox();
            String[] iterations = {"1", "5", "10", "15", "20"};
            for (String iteration : iterations) {
                try {
                    iterationList.addItem(iteration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iterationList.setBounds(176, 219, 68, 21);
            iterationList.setSelectedIndex(0);

            ComponentUtil.add(this, privateKeyLabel,
                    privateKeyScroller, generateButton,
                    clearButton, paddingLabel, hashList,
                    saltLabel, saltList,
                    iterationLabel, iterationList);

            ComponentUtil.add(parent, this);
        }
    }

    //这个类用于生成的哈希值面板
    private static class generatedPanel extends JPanel {
        private final JTextArea HashCodeText;
        private final JButton copyButton;
        private final JButton outputClearButton;

        public generatedPanel(HashView parent) {

            setLayout(null);
            setName("");
            setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Output", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(101, 208, 208)));
            setBounds(10, 279, 564, 210);

            JLabel cryptionInputLabel = new JLabel("HashCode:");
            cryptionInputLabel.setBounds(10, 24, 110, 14);
            HashCodeText = new JTextArea();
            HashCodeText.setLineWrap(true);
            HashCodeText.setBounds(10, 43, 525, 45);
            JScrollPane cryptionInputScroller = new JScrollPane(HashCodeText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            cryptionInputScroller.setBounds(10, 43, 525, 121);

            copyButton = new JButton("复制");
            copyButton.setBounds(399, 176, 136, 23);
            copyButton.setEnabled(false);
            outputClearButton = new JButton("清空");
            outputClearButton.setBounds(254, 176, 136, 23);
            outputClearButton.setEnabled(false);

            ComponentUtil.add(this, cryptionInputLabel,
                    cryptionInputScroller, copyButton,
                    outputClearButton /*,useGeneratedButton, genClearButton, clearButton*/);

            ComponentUtil.add(parent, this);
        }
    }

    //这个函数用于处理明文面板的清除按钮
    private void handleClearButton() {
        message.MessageText.setText("");
        message.clearButton.setEnabled(false);
        applyErrorProtection();
    }

    //这个函数用于处理生成密文清除按钮
    private void handleGeneratedClearButton() {
        generated.HashCodeText.setText("");
        applyErrorProtection();
    }

    //这个函数用于错误保护，不符合条件的按钮将被禁用
    private void applyErrorProtection() {
        boolean cryptoPossible = !message.MessageText.getText().isEmpty();
        message.generateButton.setEnabled(cryptoPossible);

        boolean clearIsUseful = !message.MessageText.getText().isEmpty();
        message.clearButton.setEnabled(clearIsUseful);

        boolean outputClearIsUseful = !generated.HashCodeText.getText().isEmpty();
        generated.outputClearButton.setEnabled(outputClearIsUseful);

        boolean copyIsuseful = !generated.HashCodeText.getText().isEmpty();
        generated.copyButton.setEnabled(copyIsuseful);

    }


}
