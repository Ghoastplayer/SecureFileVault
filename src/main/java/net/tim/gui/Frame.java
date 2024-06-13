package net.tim.gui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    CardLayout cardLayout = new CardLayout();
    LoginPanel loginPanel = new LoginPanel(this);
    RegisterPanel registerPanel = new RegisterPanel(this);
    VaultPanel vaultPanel;
    SettingsPanel settingsPanel;

    public Frame() {
        setTitle("File Vault");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(cardLayout);
        add(loginPanel, "loginPanel");
        add(registerPanel, "registerPanel");

        switchPanel("loginPanel");
    }

    public void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) this.getContentPane().getLayout();
        cardLayout.show(this.getContentPane(), panelName);
    }

    public void initializeVaultPanel(String username) {
        vaultPanel = new VaultPanel(this, username); // Pass this Frame to the VaultPanel
        add(vaultPanel, "vaultPanel");
        switchPanel("vaultPanel");
    }

    public void initializeSettingsPanel(String username) {
        settingsPanel = new SettingsPanel(username, this); // No need to pass this Frame to the SettingsPanel
        add(settingsPanel, "settingsPanel");
        switchPanel("settingsPanel");
    }

    public static void main(String[] args) {
        new Frame();
    }
}