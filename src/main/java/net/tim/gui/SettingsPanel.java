package net.tim.gui;

import net.tim.UserManager;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final Frame frame;
    private final String username;
    private JButton changePasswordButton;
    private JButton deleteAccountButton;

    public SettingsPanel(String username, Frame frame) {
        this.frame = frame;
        this.username = username;
        setLayout(new GridLayout(3, 1));

        Dimension buttonSize = new Dimension(150, 25); // Definieren Sie die gewünschte Größe der Schaltflächen

        changePasswordButton = new JButton("Change Password");
        deleteAccountButton = new JButton("Delete Account");

        changePasswordButton.setPreferredSize(buttonSize); // Setzen Sie die Größe der Schaltfläche "Change Password"
        deleteAccountButton.setPreferredSize(buttonSize); // Setzen Sie die Größe der Schaltfläche "Delete Account"

        add(changePasswordButton);
        add(deleteAccountButton);

        changePasswordButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JLabel currentPasswordLabel = new JLabel("Current Password:");
            JLabel newPasswordLabel = new JLabel("New Password:");
            JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");

            JPasswordField currentPasswordField = new JPasswordField();
            JPasswordField newPasswordField = new JPasswordField();
            JPasswordField confirmNewPasswordField = new JPasswordField();

            panel.add(currentPasswordLabel);
            panel.add(currentPasswordField);
            panel.add(newPasswordLabel);
            panel.add(newPasswordField);
            panel.add(confirmNewPasswordLabel);
            panel.add(confirmNewPasswordField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Check if current password is correct
                if (!UserManager.authorizeUser(username, new String(currentPasswordField.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "Incorrect current password.");
                    return;
                }
                // Check if new password and confirm new password match
                if (!new String(newPasswordField.getPassword()).equals(new String(confirmNewPasswordField.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "New password and confirm new password do not match.");
                    return;
                }

                // If everything is correct, change the password
                try {
                    UserManager.changePassword(username, new String(newPasswordField.getPassword()));
                } catch (UserNotFoundExeption ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Password successfully changed.");
                logout();
            }
        });

        deleteAccountButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                // Implementieren Sie hier die Logik zum Löschen des Kontos
                UserManager.deleteUser(username);
                JOptionPane.showMessageDialog(null, "Account successfully deleted.");
                logout();
            }
        });
    }

    private void logout() {
        frame.remove(this);
        frame.switchPanel("loginPanel");
    }
}