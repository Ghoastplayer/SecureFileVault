package net.tim.gui;

import net.tim.UserManager;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final Frame frame;
    private final JLabel messageLabel;

    public LoginPanel(Frame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());

        messageLabel = new JLabel(); // Initialize the label
        messageLabel.setForeground(Color.RED); // Set the text color to red
        GridBagConstraints messageConstraints = new GridBagConstraints();
        messageConstraints.gridx = 0;
        messageConstraints.gridy = 0;
        messageConstraints.gridwidth = 2;
        messageConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(messageLabel, messageConstraints);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
        usernameLabelConstraints.gridx = 0;
        usernameLabelConstraints.gridy = 1;
        usernameLabelConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(usernameLabel, usernameLabelConstraints);

        JTextField usernameField = new JTextField(20);
        usernameField.setToolTipText("Enter your username");
        GridBagConstraints usernameFieldConstraints = new GridBagConstraints();
        usernameFieldConstraints.gridx = 1;
        usernameFieldConstraints.gridy = 1;
        usernameFieldConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(usernameField, usernameFieldConstraints);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints passwordLabelConstraints = new GridBagConstraints();
        passwordLabelConstraints.gridx = 0;
        passwordLabelConstraints.gridy = 2;
        passwordLabelConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(passwordLabel, passwordLabelConstraints);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setToolTipText("Enter your password");
        GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
        passwordFieldConstraints.gridx = 1;
        passwordFieldConstraints.gridy = 2;
        passwordFieldConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(passwordField, passwordFieldConstraints);

        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setBackground(Color.GREEN);
        GridBagConstraints loginButtonConstraints = new GridBagConstraints();
        loginButtonConstraints.gridx = 0;
        loginButtonConstraints.gridy = 3;
        loginButtonConstraints.gridwidth = 2;
        loginButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginButtonConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(loginButton, loginButtonConstraints);
        loginButton.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            // Check if the username and password are correct
            if (UserManager.authorizeUser(username, password)) {
                passwordField.setText(""); // Clear the password field
                frame.initializeVaultPanel(username);
            } else {
                passwordField.setText(""); // Clear the password field
                messageLabel.setText("Invalid username or password.");
            }

        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 10));
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.WHITE);
        GridBagConstraints registerButtonConstraints = new GridBagConstraints();
        registerButtonConstraints.gridx = 0;
        registerButtonConstraints.gridy = 4;
        registerButtonConstraints.gridwidth = 2;
        registerButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        registerButtonConstraints.insets = new Insets(10, 10, 10, 10); // padding
        add(registerButton, registerButtonConstraints);
        registerButton.addActionListener(_ -> {
            passwordField.setText(""); // Clear the password field
            frame.switchPanel("registerPanel");
        });
    }
}