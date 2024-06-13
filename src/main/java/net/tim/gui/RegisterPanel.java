package net.tim.gui;

import net.tim.UserAlreadyExistsException;
import net.tim.UserManager;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final Frame frame;
    private final JLabel messageLabel;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    public RegisterPanel(Frame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        GridBagConstraints messageConstraints = new GridBagConstraints();
        messageConstraints.gridx = 0;
        messageConstraints.gridy = 0;
        messageConstraints.gridwidth = 3;
        messageConstraints.insets = new Insets(10, 10, 10, 10);
        add(messageLabel, messageConstraints);

        // Username
        usernameField = addLabelAndTextField("Username:", 1);
        // Password
        passwordField = addLabelAndPasswordField("Password:", 2);
        // Confirm Password
        confirmPasswordField = addLabelAndPasswordField("Confirm Password:", 3);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setBackground(Color.GREEN);
        GridBagConstraints registerButtonConstraints = new GridBagConstraints();
        registerButtonConstraints.gridx = 0;
        registerButtonConstraints.gridy = 4;
        registerButtonConstraints.gridwidth = 3;
        registerButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        registerButtonConstraints.insets = new Insets(10, 10, 10, 10);
        add(registerButton, registerButtonConstraints);
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            try {
                UserManager.createUser(username, password);
                frame.switchPanel("loginPanel");
            } catch (UserAlreadyExistsException ex) {
                messageLabel.setText("User already exists.");
            }
        });

        // Login Button
        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 10));
        loginButton.setBackground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        GridBagConstraints loginButtonConstraints = new GridBagConstraints();
        loginButtonConstraints.gridx = 0;
        loginButtonConstraints.gridy = 5;
        loginButtonConstraints.gridwidth = 3;
        loginButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        loginButtonConstraints.insets = new Insets(10, 10, 10, 10);
        add(loginButton, loginButtonConstraints);
        loginButton.addActionListener(_ -> frame.switchPanel("loginPanel"));
    }

    private JTextField addLabelAndTextField(String labelText, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = gridy;
        labelConstraints.insets = new Insets(10, 10, 10, 10);
        add(label, labelConstraints);

        JTextField textField = new JTextField(20);
        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = gridy;
        textFieldConstraints.gridwidth = 2;
        textFieldConstraints.insets = new Insets(10, 10, 10, 10);
        add(textField, textFieldConstraints);

        return textField;
    }

    private JPasswordField addLabelAndPasswordField(String labelText, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = gridy;
        labelConstraints.insets = new Insets(10, 10, 10, 10);
        add(label, labelConstraints);

        JPasswordField passwordField = new JPasswordField(20);
        GridBagConstraints passwordFieldConstraints = new GridBagConstraints();
        passwordFieldConstraints.gridx = 1;
        passwordFieldConstraints.gridy = gridy;
        passwordFieldConstraints.gridwidth = 2;
        passwordFieldConstraints.insets = new Insets(10, 10, 10, 10);
        add(passwordField, passwordFieldConstraints);

        return passwordField;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}