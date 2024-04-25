import com.formdev.flatlaf.FlatDarkLaf;
import com.rsaApp.Window.MainWindow;
import com.rsaApp.Window.View.MainView;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //使用暗色主题FlatDarkLaf()或FlatDarculaLaf()主题
        applyFlatDarkLafLook();
        //初始化窗口
        MainWindow mainWindow = new MainWindow();
        //显示窗口
        mainWindow.showWindow();
    }

    private static void applyFlatDarkLafLook() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
        }
    }


}
