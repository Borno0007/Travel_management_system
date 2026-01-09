package com.tms.config;

import java.util.Properties;

public class EmailConfig {
    // Email configuration constants
    // For Gmail: Enable 2-factor authentication and create an App Password
    // Go to: https://myaccount.google.com/apppasswords
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "your.email@gmail.com"; // Change this
    private static final String EMAIL_PASSWORD = "your-app-password"; // Change this (use App Password, not regular password)
    private static final String FROM_EMAIL = "your.email@gmail.com"; // Change this
    private static final String FROM_NAME = "Travel Management System";
    
    public static Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return properties;
    }
    
    public static String getUsername() {
        return EMAIL_USERNAME;
    }
    
    public static String getPassword() {
        return EMAIL_PASSWORD;
    }
    
    public static String getFromEmail() {
        return FROM_EMAIL;
    }
    
    public static String getFromName() {
        return FROM_NAME;
    }
}
