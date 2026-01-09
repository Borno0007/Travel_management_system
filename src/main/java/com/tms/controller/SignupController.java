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

public class SignupController {
    
    @FXML
    private TextField fullNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Label messageLabel;
    
    private UserDAO userDAO = new UserDAO();
    
    @FXML
    private void handleSignup() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validation
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields", "error");
            return;
        }
        
        if (!email.contains("@")) {
            showMessage("Please enter a valid email address", "error");
            return;
        }
        
        if (phone.length() < 10) {
            showMessage("Please enter a valid phone number", "error");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match", "error");
            return;
        }
        
        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters", "error");
            return;
        }
        
        if (userDAO.emailExists(email)) {
            showMessage("Email already registered", "error");
            return;
        }
        
        // Register user
        if (userDAO.registerUser(fullName, email, phone, password)) {
            showMessage("Registration successful! Redirecting to login...", "success");
            
            // Redirect to login after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> handleLoginLink());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showMessage("Registration failed. Please try again.", "error");
        }
    }
    
    @FXML
    private void handleLoginLink() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Management System - Login");
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
