package com.rsaApp.Window.View;

import com.rsaApp.Window.View.subView.HashView;
import com.rsaApp.Window.View.subView.RsaView;

import javax.swing.*;

public class MainView extends JTabbedPane {

    public MainView() {
        addTab("OAEP-RSA", null, new RsaView(), "Encryption features with rsa key pair");
            // addTab("Module AES", null, new AesView(), "Encryption features with aes key");
        addTab("HASHCode", null, new HashView(), "Hash generation from readable text");
    }

}
