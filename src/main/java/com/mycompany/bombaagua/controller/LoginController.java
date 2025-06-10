package com.mycompany.bombaagua.controller;

import com.mycompany.bombaagua.dao.UserDAO;
import com.mycompany.bombaagua.exceptions.DAOExceptions;
import com.mycompany.bombaagua.model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private Button buttonClickCreate;
    
    @FXML
    private void handleToggleClickCreate(ActionEvent event) throws IOException {
        // 1. Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/bombaagua/adduser.fxml"));
        Parent root = loader.load();

        // 2. Create a new Stage (window)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Dialog Window");
        dialogStage.setScene(new Scene(root, 300, 200));

        // 3. (Optional) Configure modality
        dialogStage.initModality(Modality.WINDOW_MODAL); // Blocks parent window
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Sets parent

        // 4. Show the new window
        dialogStage.show();
    }
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    
    private final UserDAO userDao = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty!");
            return;
        }

        try {
            if (isValidLogin(username, password)) {
                openMainApp(username);
            } else {
                showError("Invalid username or password!");
            }
        } catch (SQLException | DAOExceptions e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
    }

    private void openMainApp(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/bombaagua/canvas.fxml"));
            Parent root = loader.load();
            
            Stage mainStage = new Stage();
            mainStage.setTitle("Welcome, " + username + "!");
            mainStage.setScene(new Scene(root, 800, 600));
            mainStage.setMinWidth(600);
            mainStage.setMinHeight(400);

            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load application.");
        }
    }
}
