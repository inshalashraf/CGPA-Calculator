package com.cgpa.frontend.ui;

import com.cgpa.backend.service.FacultyAuthService;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean authenticated = false;

    public LoginDialog(Window owner, FacultyAuthService authService) {
        super(owner, "Faculty Login", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; form.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; form.add(passwordField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(cancelBtn);
        buttons.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            char[] pass = passwordField.getPassword();
            if (authService.authenticate(user, pass)) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });
        cancelBtn.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setSize(360, 180);
        setLocationRelativeTo(owner);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

