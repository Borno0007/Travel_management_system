package com.tms.controller;

import com.tms.database.UserDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label messageLabel;
    
    private UserDAO userDAO = new UserDAO();
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields", "error");
            return;
        }
        
        if (userDAO.loginUser(email, password)) {
            showMessage("Login successful!", "success");
            try {
                // Load home page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                Parent root = loader.load();
                
                HomeController homeController = loader.getController();
                homeController.setUserEmail(email);
                
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Travel Management System - Home");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error loading home page", "error");
            }
        } else {
            showMessage("Invalid email or password", "error");
        }
    }
    
    @FXML
    private void handleSignupLink() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Management System - Sign Up");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        if (type.equals("error")) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
}
