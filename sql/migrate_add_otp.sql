-- Migration script to add OTP fields to existing users table
-- Run this if you already have the database created without OTP fields

USE travel_management_db;

-- Add OTP-related columns if they don't exist
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS otp VARCHAR(10) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS otp_expiry TIMESTAMP DEFAULT NULL,
ADD COLUMN IF NOT EXISTS is_verified BOOLEAN DEFAULT FALSE;

-- Optional: Mark all existing users as verified (if upgrading from old version)
-- UPDATE users SET is_verified = TRUE WHERE is_verified = FALSE;

SELECT 'Migration completed successfully!' AS Status;
