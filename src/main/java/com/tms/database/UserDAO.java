package com.tms.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tms.util.PasswordUtils;

public class UserDAO {
    
    public boolean registerUser(String fullName, String email, String phone, String password) {
        String query = "INSERT INTO users (full_name, email, phone, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, PasswordUtils.hashPassword(password));
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean loginUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return PasswordUtils.verifyPassword(password, hashedPassword);
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
            return false;
        }
    }
    
    public boolean emailExists(String email) {
        String query = "SELECT email FROM users WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            return false;
        }
    }
    
    public String getUserName(String email) {
        String query = "SELECT full_name FROM users WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("full_name");
            }
            return null;
            
        } catch (SQLException e) {
            System.err.println("Error getting user name: " + e.getMessage());
            return null;
        }
    }
}
