package com.rsaApp.shared;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
}