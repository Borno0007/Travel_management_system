package com.tms.controller;

import com.tms.database.UserDAO;
import com.tms.service.EmailService;
import com.tms.util.OTPUtils;

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
        
        // Check if email already exists and is verified
        if (userDAO.emailExists(email) && userDAO.isEmailVerified(email)) {
            showMessage("Email already registered and verified", "error");
            return;
        }
        
        // Generate OTP
        String otp = OTPUtils.generateOTP();
        
        // Create pending user with OTP
        if (userDAO.createPendingUser(fullName, email, phone, password, otp)) {
            // Send OTP email
            showMessage("Sending verification email...", "success");
            
            // Send email in a separate thread to avoid blocking UI
            new Thread(() -> {
                boolean emailSent = EmailService.sendOTPEmail(email, fullName, otp);
                
                javafx.application.Platform.runLater(() -> {
                    if (emailSent) {
                        showMessage("OTP sent to your email! Redirecting to verification...", "success");
                        
                        // Redirect to OTP verification page after 2 seconds
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                javafx.application.Platform.runLater(() -> handleOTPVerification(email));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } else {
                        showMessage("Error sending OTP email. Please check email configuration.", "error");
                    }
                });
            }).start();
        } else {
            showMessage("Registration failed. Please try again.", "error");
        }
    }
    
    private void handleOTPVerification(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/otp_verification.fxml"));
            Parent root = loader.load();
            
            // Pass email to OTP verification controller
            OTPVerificationController controller = loader.getController();
            controller.setEmail(email);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Management System - Verify Email");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading verification page", "error");
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
