package com.rsaApp.Window.View;

import com.rsaApp.Window.View.subView.HashView;
import com.rsaApp.Window.View.subView.RsaView;
import com.rsaApp.Window.View.subView.aboutView;

import javax.swing.*;

public class MainView extends JTabbedPane {
    public MainView() {
        addTab("OAEP-RSA", null, new RsaView(), "Encryption features with rsa key pair");
        addTab("HASHCode", null, new HashView(), "Hash generation from readable text");
        addTab("About", null, new aboutView(), "About the application");
    }
}
