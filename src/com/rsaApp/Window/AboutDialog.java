package com.rsaApp.Window;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    public AboutDialog(JFrame parent) {
        super(parent, "关于", true);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><h1>MyApp</h1><p>版本 1.0</p><p>© 2024 MyApp 公司</p></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Main Application");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton aboutButton = new JButton("关于");
            aboutButton.addActionListener(e -> {
                AboutDialog dialog = new AboutDialog(frame);
                dialog.setVisible(true);
            });

            frame.getContentPane().add(aboutButton, BorderLayout.CENTER);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

