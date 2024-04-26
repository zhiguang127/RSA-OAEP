package com.rsaApp.Window;

//采用Swing编写GUI界面

import com.rsaApp.Window.View.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

//MainWindow：用于显示主窗口的类。
public class MainWindow extends JFrame {

    private MainView defaultView;

    public MainWindow() throws Exception {
        this.setTitle("RSA");
        this.setBounds(100, 100, 600, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
        //设置窗口大小和关闭方式
        setCloseWay();
        //设置窗口图标
        setIcon();
        this.defaultView = new MainView();
        setContentPane(this.defaultView);
    }

    private void setCloseWay() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(MainWindow.this, "你真的要退出吗？", "退出程序", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void setIcon() {
        URL urlIcon = this.getClass().getResource("/resources/rsa.png");
        ImageIcon icon = null;
        if (urlIcon != null) {
            icon = new ImageIcon(urlIcon);
        }
        Image image = null;
        if (icon != null) {
            image = icon.getImage();
        }
        this.setIconImage(image);
    }

    public void showWindow() {
        this.setVisible(true);
    }
}
