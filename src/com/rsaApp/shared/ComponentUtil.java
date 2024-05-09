package com.rsaApp.shared;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.PublicKey;


// 这个类就是把一些常用的方法封装起来，方便调用，都是静态方法
// 网上有很多这种工具类，直接拿来用就行
public final class ComponentUtil {

    private ComponentUtil() {
    }

    // 把多个组件添加到一个容器中
    public static void add(Container container, Component... components) {
        for (Component component : components)
            container.add(component);
    }

    // 给多个组件添加键盘监听器，当键盘按键释放时执行action
    public static void onKeyReleased(Runnable action, Component... components) {
        for (Component component : components) {
            component.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (component.isEnabled())
                        action.run();
                }
            });
        }
    }
    // 给多个组件添加鼠标监听器，当鼠标释放时执行action
    public static void onMouseReleased(Runnable action, Component... components) {
        for (Component component : components) {
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (component.isEnabled())
                        action.run();
                }
            });
        }
    }


    //根据选择的盐值，返回对应的盐值
    public static String getSalt(String chosedSalt){
        String salt;
        switch (chosedSalt)
        {
            case "无":
                break;
            case "盐值1":
                salt = "一段未醒 又做一段\n" +
                        "如果这画面有开关\n" +
                        "从期待走到不堪\n" +
                        "结局不好看\n" +
                        "人总需要记住遗憾";
                return salt;
            case "盐值2":
                salt = "烏蒙山連着山外山 月光灑下了響水灘\n" +
                        "有沒有人能告訴我 可是蒼天對你在呼喚！";
                return salt;
            case "盐值3":
                salt = "NJUPT";
                return salt;
        }
       return null;
    }


}