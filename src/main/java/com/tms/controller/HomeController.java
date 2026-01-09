package com.tms.controller;

import com.tms.database.UserDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label infoLabel;
    
    private String userEmail;
    private UserDAO userDAO = new UserDAO();
    
    public void setUserEmail(String email) {
        this.userEmail = email;
        String userName = userDAO.getUserName(email);
        if (userName != null) {
            welcomeLabel.setText("Welcome, " + userName + "!");
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Travel Management System - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
