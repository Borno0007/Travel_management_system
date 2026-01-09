# Email Configuration Guide for OTP Verification

## Overview
This Travel Management System now includes OTP-based email verification for new user registrations. When a user signs up, they receive a 6-digit OTP code via email that must be entered to complete the registration process.

## Prerequisites
- A Gmail account with 2-factor authentication enabled
- Google App Password (not your regular Gmail password)

## Step-by-Step Configuration

### 1. Enable 2-Factor Authentication on Google Account
1. Go to your Google Account settings: https://myaccount.google.com
2. Navigate to **Security** → **2-Step Verification**
3. Follow the instructions to enable 2-factor authentication if not already enabled

### 2. Create an App Password
1. Go to https://myaccount.google.com/apppasswords
2. Sign in if prompted
3. Under "App passwords", create a new app password:
   - **App name**: "Travel Management System" (or any name you prefer)
   - Click **Create**
4. **Copy the 16-character password** displayed (without spaces)
5. Click **Done**

### 3. Configure EmailConfig.java
1. Open the file: `src/main/java/com/tms/config/EmailConfig.java`
2. Update the following constants with your information:

```java
private static final String EMAIL_USERNAME = "your.email@gmail.com";  // Your Gmail address
private static final String EMAIL_PASSWORD = "xxxx xxxx xxxx xxxx";   // Your App Password (16 characters)
private static final String FROM_EMAIL = "your.email@gmail.com";      // Same as EMAIL_USERNAME
```

**Example:**
```java
private static final String EMAIL_USERNAME = "john.doe@gmail.com";
private static final String EMAIL_PASSWORD = "abcd efgh ijkl mnop";
private static final String FROM_EMAIL = "john.doe@gmail.com";
```

### 4. Update Database Schema
If you already have an existing database, you need to add the new OTP fields:

**Option A: Fresh Installation**
Run the standard initialization script:
```sql
source sql/init_db.sql
```

**Option B: Existing Database Migration**
Run the migration script to add OTP fields to existing users table:
```sql
source sql/migrate_add_otp.sql
```

Or manually add the columns:
```sql
USE travel_management_db;

ALTER TABLE users 
ADD COLUMN IF NOT EXISTS otp VARCHAR(10) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS otp_expiry TIMESTAMP DEFAULT NULL,
ADD COLUMN IF NOT EXISTS is_verified BOOLEAN DEFAULT FALSE;

-- Optional: Mark all existing users as verified
UPDATE users SET is_verified = TRUE WHERE is_verified = FALSE;
```

## How It Works

### Registration Flow
1. User fills out the signup form with:
   - Full Name
   - Email
   - Phone
   - Password
   - Confirm Password

2. System validates the input and generates a 6-digit OTP

3. User data is stored in the database with `is_verified = FALSE`

4. An email is sent to the user's email address containing:
   - The 6-digit OTP
   - Expiry notice (OTP valid for 5 minutes)

5. User is redirected to the OTP verification page

6. User enters the OTP received via email

7. System verifies:
   - OTP matches the one stored in database
   - OTP has not expired (within 5 minutes)

8. If valid:
   - Account is marked as verified (`is_verified = TRUE`)
   - OTP and expiry are cleared from database
   - User is redirected to login page

### Login Flow
- Users can only login if their email is verified
- Unverified accounts will be rejected at login

## Troubleshooting

### Email Not Received
1. **Check Spam/Junk Folder**: Gmail might flag automated emails as spam
2. **Verify App Password**: Ensure you copied the 16-character password correctly
3. **Check Email Configuration**: Verify `EMAIL_USERNAME`, `EMAIL_PASSWORD`, and `FROM_EMAIL` in `EmailConfig.java`
4. **Internet Connection**: Ensure your server has internet access to connect to Gmail's SMTP server
5. **Gmail Security**: Ensure 2-factor authentication is enabled and app password is valid

### "Error sending OTP email" Message
- Check the application console for detailed error messages
- Common causes:
  - Invalid App Password
  - 2-Factor Authentication not enabled
  - Firewall blocking SMTP port 587
  - No internet connection

### OTP Expired
- OTP is valid for 5 minutes only
- If expired, user must restart the registration process
- Consider increasing the expiry time in `OTPUtils.java` if needed:
```java
return differenceInMinutes > 5; // Change 5 to desired minutes
```

### Cannot Login After Registration
- Ensure the account is verified (check `is_verified` column in database)
- If account shows as not verified, re-register or manually set `is_verified = TRUE` in database

## Email Template Customization
To customize the OTP email template, edit the `createOTPEmailBody` method in:
`src/main/java/com/tms/service/EmailService.java`

You can modify:
- Email styling (colors, fonts)
- Company name/branding
- Email copy/text
- Logo/images (add as base64 or external links)

## Security Considerations

### Best Practices
✅ **DO:**
- Use App Passwords instead of regular Gmail passwords
- Keep `EmailConfig.java` secure and never commit passwords to public repositories
- Use environment variables for production deployments
- Monitor failed OTP attempts to detect potential abuse
- Consider implementing rate limiting for OTP requests

❌ **DO NOT:**
- Share your App Password
- Commit sensitive credentials to version control
- Use regular Gmail password in `EMAIL_PASSWORD`
- Disable 2-factor authentication

### Production Deployment
For production environments, consider:
1. Using environment variables instead of hardcoded credentials:
```java
private static final String EMAIL_USERNAME = System.getenv("EMAIL_USERNAME");
private static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");
```

2. Using a dedicated email service (SendGrid, Amazon SES, etc.) instead of Gmail
3. Implementing rate limiting to prevent OTP spam
4. Adding CAPTCHA to prevent automated abuse
5. Logging OTP generation and verification attempts for security auditing

## Alternative Email Providers
While this guide uses Gmail, you can configure other SMTP providers by updating `EmailConfig.java`:

### Example: Outlook/Hotmail
```java
private static final String SMTP_HOST = "smtp-mail.outlook.com";
private static final String SMTP_PORT = "587";
```

### Example: Yahoo Mail
```java
private static final String SMTP_HOST = "smtp.mail.yahoo.com";
private static final String SMTP_PORT = "587";
```

## Testing
1. Complete a test registration with a real email address you own
2. Check if the OTP email arrives within 1-2 minutes
3. Enter the OTP on the verification page
4. Verify successful login after OTP verification
5. Try using an expired OTP (wait 6+ minutes) to test expiry logic

## Support
If you encounter issues:
1. Check the application console for error messages
2. Verify all configuration steps were completed
3. Test email connectivity with a simple SMTP test tool
4. Review the error logs in the application output

## Database Schema Reference

### Updated `users` Table
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    otp VARCHAR(10) DEFAULT NULL,              -- New: Stores the OTP
    otp_expiry TIMESTAMP DEFAULT NULL,         -- New: Stores OTP creation time
    is_verified BOOLEAN DEFAULT FALSE,         -- New: Email verification status
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```
