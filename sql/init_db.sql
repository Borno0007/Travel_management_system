-- Travel Management System Database Initialization Script
-- For MySQL 8.0+

-- Create database
CREATE DATABASE IF NOT EXISTS travel_management_db;

-- Use the database
USE travel_management_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX idx_email ON users(email);

-- Display success message
SELECT 'Database and tables created successfully!' AS Status;

-- Optional: Display table structure
DESCRIBE users;
