/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bombaagua.controller;

//@author aoxle

import com.mycompany.bombaagua.App;
import com.mycompany.bombaagua.dao.UserDAO;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import com.mycompany.bombaagua.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserController {
    
    public class LoginController {

        @FXML
        private TextField usernameField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private Label errorLabel;

        private final UserDAO userDao = new UserDAO();

        @FXML
        private void handleLogin() {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and password cannot be empty!");
                return;
            }

            try {
                // Check credentials against database
                if (isValidLogin(username, password)) {
                    // Close login window and open main app
                    openMainApp(username);
                } else {
                    showError("Invalid username or password!");
                }
            } catch (SQLException | DAOExceptions e) {
                showError("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Validate credentials against database
        private boolean isValidLogin(String username, String password) throws SQLException, DAOExceptions {
            List<User> users = userDao.getUser();

            for (User user : users) {
                if (user.getUser_name().equals(username) && 
                    user.getUser_password().equals(password)) {
                    return true;
                }
            }
            return false;
        }

        // Show error message
        private void showError(String message) {
            errorLabel.setText(message);
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setVisible(true);
        }

        // Open main application window
        private void openMainApp(String username) {
            try {
                // Load the main app FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/bombaagua/canvas.fxml"));
                Parent root = loader.load();

                // Pass the username to the main controller if needed
                // MainAppController mainController = loader.getController();
                // mainController.setLoggedInUser(username);

                // Create a new stage for the main app
                Stage mainStage = new Stage();
                mainStage.setTitle("Welcome, " + username + "!");
                mainStage.setScene(new Scene(root, 800, 600));
                mainStage.setMinWidth(600);
                mainStage.setMinHeight(400);

                // Close the login window
                Stage loginStage = (Stage) usernameField.getScene().getWindow();
                loginStage.close();

                // Show the main app
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to load application.");
            }
        }
    }

    

    public class CreateController {

        @FXML
        private TextField usernameField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private PasswordField passwordField1;

        @FXML
        private Label errorLabel;

        @FXML
        private Button createButton1; // Confirm button

        @FXML
        private Button createButton11; // Cancel button

        private final UserDAO userDao = new UserDAO();

        @FXML
        private void handleCreate() {
            try {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String confirmPassword = passwordField1.getText().trim();

                // Validate inputs
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showError("All fields are required");
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

                // Create new user
                User newUser = new User();
                newUser.setUser_name(username);
                newUser.setUser_password(password); // In real app, you should hash this
                newUser.setUser_email(""); // Add email field if needed

                // Add user to database
                userDao.addUser(newUser);

                // Show success and close window or navigate
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
            // Implement your own ID generation logic
            return "user_" + System.currentTimeMillis();
        }
    }
}