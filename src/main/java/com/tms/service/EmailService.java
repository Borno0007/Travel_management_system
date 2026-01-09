package com.tms.service;

import com.tms.config.EmailConfig;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    
    /**
     * Send OTP email to user
     */
    public static boolean sendOTPEmail(String toEmail, String userName, String otp) {
        try {
            Properties properties = EmailConfig.getMailProperties();
            
            // Create session with authenticator
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        EmailConfig.getUsername(), 
                        EmailConfig.getPassword()
                    );
                }
            });
            
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EmailConfig.getFromEmail(), EmailConfig.getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Travel Management System - Email Verification");
            
            // Create email body
            String emailBody = createOTPEmailBody(userName, otp);
            message.setContent(emailBody, "text/html; charset=utf-8");
            
            // Send email
            Transport.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Create HTML email body for OTP
     */
    private static String createOTPEmailBody(String userName, String otp) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><meta charset='UTF-8'></head>" +
               "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
               "  <div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
               "    <h2 style='color: #2c3e50; text-align: center;'>Travel Management System</h2>" +
               "    <h3 style='color: #34495e;'>Email Verification</h3>" +
               "    <p>Dear " + userName + ",</p>" +
               "    <p>Thank you for registering with Travel Management System. To complete your registration, please use the following One-Time Password (OTP):</p>" +
               "    <div style='background-color: #ecf0f1; padding: 15px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0; border-radius: 5px;'>" +
               "      " + otp +
               "    </div>" +
               "    <p style='color: #e74c3c;'><strong>Important:</strong> This OTP is valid for 5 minutes only.</p>" +
               "    <p>If you did not request this registration, please ignore this email.</p>" +
               "    <hr style='margin: 20px 0; border: none; border-top: 1px solid #ddd;'>" +
               "    <p style='font-size: 12px; color: #7f8c8d; text-align: center;'>This is an automated email. Please do not reply.</p>" +
               "  </div>" +
               "</body>" +
               "</html>";
    }
}
