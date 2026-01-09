package com.tms.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.tms.util.OTPUtils;
import com.tms.util.PasswordUtils;

public class UserDAO {
    
    /**
     * Store user registration data with OTP (pending verification)
     */
    public boolean createPendingUser(String fullName, String email, String phone, String password, String otp) {
        // First check if email is already verified
        if (emailExists(email) && isEmailVerified(email)) {
            System.err.println("Email already registered and verified");
            return false;
        }
        
        String query = "INSERT INTO users (full_name, email, phone, password, otp, otp_expiry, is_verified) VALUES (?, ?, ?, ?, ?, ?, FALSE) " +
                      "ON DUPLICATE KEY UPDATE full_name=?, phone=?, password=?, otp=?, otp_expiry=?, is_verified=FALSE";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            Timestamp otpExpiry = new Timestamp(System.currentTimeMillis());
            String hashedPassword = PasswordUtils.hashPassword(password);
            
            // For INSERT
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, hashedPassword);
            pstmt.setString(5, otp);
            pstmt.setTimestamp(6, otpExpiry);
            
            // For UPDATE (in case email already exists but not verified)
            pstmt.setString(7, fullName);
            pstmt.setString(8, phone);
            pstmt.setString(9, hashedPassword);
            pstmt.setString(10, otp);
            pstmt.setTimestamp(11, otpExpiry);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating pending user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify OTP and activate user account
     */
    public boolean verifyOTP(String email, String otp) {
        String selectQuery = "SELECT otp, otp_expiry FROM users WHERE email = ? AND is_verified = FALSE";
        String updateQuery = "UPDATE users SET is_verified = TRUE, otp = NULL, otp_expiry = NULL WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            
            selectStmt.setString(1, email);
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                String storedOTP = rs.getString("otp");
                Timestamp otpExpiry = rs.getTimestamp("otp_expiry");
                
                // Check if OTP matches
                if (storedOTP != null && storedOTP.equals(otp)) {
                    // Check if OTP is not expired
                    if (!OTPUtils.isOTPExpired(otpExpiry)) {
                        // Activate the account
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, email);
                            int result = updateStmt.executeUpdate();
                            return result > 0;
                        }
                    } else {
                        System.err.println("OTP has expired");
                        return false;
                    }
                } else {
                    System.err.println("Invalid OTP");
                    return false;
                }
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error verifying OTP: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Original registration method - now deprecated, use createPendingUser + verifyOTP instead
     */
    @Deprecated
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
        String query = "SELECT password, is_verified FROM users WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean isVerified = rs.getBoolean("is_verified");
                if (!isVerified) {
                    System.err.println("Email not verified");
                    return false;
                }
                
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
    
    public boolean isEmailVerified(String email) {
        String query = "SELECT is_verified FROM users WHERE email = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("is_verified");
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error checking email verification: " + e.getMessage());
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
