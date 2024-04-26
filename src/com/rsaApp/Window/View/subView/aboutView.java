package com.rsaApp.Window.View.subView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class aboutView extends JPanel {

    public aboutView() {
        setLayout(null);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("About");
        setBorder(titledBorder);

        JLabel label = new JLabel();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resources/rsa.png"));

        imageIcon.setImage(imageIcon.getImage().getScaledInstance(130, 130, Image.SCALE_DEFAULT));
        label.setIcon(imageIcon);
        label.setBounds(126, 96, 136, 136); // 设置图片位置和大小
        add(label);

        JTextArea textArea = new JTextArea();
        textArea.setText("RSA-OAEP加密工具\n" + "版本号：1.1.0\n" + "作者：juvenile\n" + "学校：NJUPT\n" + "学号：B21031314");
        textArea.setFont(new Font("微软雅黑", Font.BOLD, 16));
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBounds(258, 114, 280, 120);
        add(textArea);

        JTextArea aboutTextArea = new JTextArea();
        aboutTextArea.setText("关于OAEP填充\n" + "OAEP算法是费斯妥密码的一种形式，它使用一对随机预言G和H在进行非对称加密之前处理明文。OAEP与任何安全的陷门单向置换\n" + "结合使用在随机预言模型中被证明是一种在选择明文攻击（IND-CPA）下语义安全的组合方案。当使用某些陷门置换（如RSA）实现时，OAEP也被证明可以抵抗选择密文攻击。");
        aboutTextArea.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 14));
        aboutTextArea.setEditable(false);
        aboutTextArea.setWrapStyleWord(true);
        aboutTextArea.setLineWrap(true);
        aboutTextArea.setBounds(36, 250, 510, 200);
        add(aboutTextArea);
        setPreferredSize(new Dimension(300, 400));
    }


}