package com.tms.controller;

import com.tms.database.UserDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OTPVerificationController {
    
    @FXML
    private TextField otpField;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private Label emailLabel;
    
    private String email;
    private UserDAO userDAO = new UserDAO();
    
    public void setEmail(String email) {
        this.email = email;
        emailLabel.setText("Enter the OTP sent to: " + email);
    }
    
    @FXML
    private void handleVerifyOTP() {
        String otp = otpField.getText().trim();
        
        if (otp.isEmpty()) {
            showMessage("Please enter the OTP", "error");
            return;
        }
        
        if (otp.length() != 6) {
            showMessage("OTP must be 6 digits", "error");
            return;
        }
        
        // Verify OTP
        if (userDAO.verifyOTP(email, otp)) {
            showMessage("Email verified successfully! Redirecting to login...", "success");
            
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
            showMessage("Invalid or expired OTP. Please try again.", "error");
        }
    }
    
    @FXML
    private void handleLoginLink() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) otpField.getScene().getWindow();
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
