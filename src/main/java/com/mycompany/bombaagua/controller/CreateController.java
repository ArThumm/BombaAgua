package com.mycompany.bombaagua.controller;

import com.mycompany.bombaagua.dao.UserDAO;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import com.mycompany.bombaagua.model.User;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateController {
    
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    
    private final UserDAO userDao = new UserDAO();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    private void handleCreate() {
        try {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            // Validate inputs
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields are required");
                return;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                showError("Please enter a valid email address");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (password.length() < 6) {
                showError("Password must be at least 6 characters");
                return;
            }

            // Create and save new user
            User newUser = new User();
            newUser.setUser_name(username);
            newUser.setUser_email(email);
            newUser.setUser_password(password); // Note: Should hash password in production

            userDao.addUser(newUser);
            showSuccess("User created successfully!");
            closeWindow();
            
        } catch (SQLException | DAOExceptions e) {
            showError("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private String generateUserId() {
        return "user_" + System.currentTimeMillis();
    }
}