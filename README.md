# Travel Management System - Bangladesh

A JavaFX desktop application for managing travel services in Bangladesh with MySQL backend for user authentication.

## Features

- **User Authentication**: Secure login and signup with password hashing (SHA-256)
- **Email Verification**: OTP-based email verification for new registrations
- **MySQL Database**: Persistent storage for user accounts
- **Minimal Homepage**: Simple dashboard showing popular destinations and services in Bangladesh
- **JavaFX UI**: Modern, responsive user interface

## Prerequisites

Before running the application, ensure you have:

1. **Java JDK 17 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/

2. **Apache Maven**
   - Download from: https://maven.apache.org/download.cgi
   - Add Maven to your PATH

3. **MySQL Server 8.0+**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Install and start MySQL service

4. **JavaFX SDK** (Maven will download this automatically)

## Database Setup

1. **Start MySQL Server**
   ```powershell
   net start MySQL80
   ```

2. **Create the Database**
   - Open MySQL Command Line Client or MySQL Workbench
   - Login with your root credentials
   - Run the initialization script:
   ```sql
   source C:/Users/jalal/Desktop/Travel_management_system/sql/init_db.sql
   ```
   
   Or manually execute:
   ```sql
   CREATE DATABASE IF NOT EXISTS travel_management_db;
   USE travel_management_db;
   
   CREATE TABLE IF NOT EXISTS users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       full_name VARCHAR(100) NOT NULL,
       email VARCHAR(100) NOT NULL UNIQUE,
       phone VARCHAR(20) NOT NULL,
       password VARCHAR(255) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

3. **Configure Database Connection**
   - Open `src/main/java/com/tms/database/Database.java`
   - Update the MySQL credentials if needed:
   ```java
   private static final String USER = "root";
   private static final String PASSWORD = ""; // Your MySQL password
   ```

4. **Configure Email Settings (for OTP verification)**
   - Open `src/main/java/com/tms/config/EmailConfig.java`
   - Update email configuration with your Gmail credentials:
   ```java
   private static final String EMAIL_USERNAME = "your.email@gmail.com";
   private static final String EMAIL_PASSWORD = "your-app-password";
   private static final String FROM_EMAIL = "your.email@gmail.com";
   ```
   
   **Important:** For Gmail, you need to:
   - Enable 2-factor authentication on your Google account
   - Generate an App Password at: https://myaccount.google.com/apppasswords
   - Use the App Password (not your regular Gmail password) in `EMAIL_PASSWORD`
   
   **Note:** If email is not configured, registration will show an error when sending OTP.

## Installation & Running

1. **Navigate to the project directory**
   ```powershell
   cd C:\Users\jalal\Desktop\Travel_management_system
   ```

2. **Build the project**
   ```powershell
   mvn clean install
   ```

3. **Run the application**
   ```powershell
   mvn javafx:run
   ```

## Project Structure

```
Travel_management_system/
├── pom.xml                          # Maven configuration
├── sql/
│   └── init_db.sql                  # Database initialization script
├── src/
│   └── main/
│       ├── java/com/tms/
│       │   ├── Main.java            # Application entry point
│       │   ├── controller/
│       │   │   ├── LoginController.java
│       │   │   ├── SignupController.java
│       │   │   └── HomeController.java
│       │   ├── database/
│       │   │   ├── Database.java    # Database connection
│       │   │   └── UserDAO.java     # User data access object
│       │   └── util/
│       │       └── PasswordUtils.java # Password hashing utility
│       └── resources/
│           └── fxml/
│               ├── login.fxml       # Login page UI
│               ├── signup.fxml      # Signup page UI
│               └── home.fxml        # Home page UI
└── README.md
```

## Usage

### Sign Up
1. Launch the application
2. Click "Sign Up" link on the login page
3. Fill in your details:
   - Full Name
   - Email
   - Phone Number (minimum 10 digits)
   - Password (minimum 6 characters)
4. Click "Sign Up" button
5. Check your email for the 6-digit OTP (One-Time Password)
6. Enter the OTP on the verification page (valid for 5 minutes)
7. You'll be redirected to login page after successful verification

### Login
1. Enter your registered email and password
2. Click "Login" button
3. You'll be redirected to the home page upon successful login

### Home Page
- View popular destinations in Bangladesh
- See available travel services
- Quick travel information
- Logout option

## Bangladesh Travel Highlights

The system features information about:
- **Cox's Bazar**: World's longest natural sea beach
- **Sundarbans**: Largest mangrove forest and UNESCO World Heritage Site
- **Sylhet**: Famous for tea gardens and natural beauty
- **Bandarban**: Beautiful hill tracts region

## Troubleshooting

### Common Issues

1. **MySQL Connection Error**
   - Ensure MySQL service is running
   - Check database credentials in `Database.java`
   - Verify database `travel_management_db` exists

2. **Email Sending Error / OTP Not Received**
   - Check email configuration in `EmailConfig.java`
   - Ensure you're using Gmail App Password (not regular password)
   - Enable 2-factor authentication on your Google account
   - Check spam/junk folder for the OTP email
   - Verify internet connection is working

3. **JavaFX Runtime Error**
   - Make sure you're using Java 17 or higher
   - Maven should automatically download JavaFX dependencies

4. **Build Failures**
   - Run `mvn clean` then `mvn install`
   - Check internet connection for Maven dependencies

5. **OTP Expired**
   - OTP is valid for 5 minutes only
   - Request a new registration if OTP expires

## Security Note

This application uses SHA-256 for password hashing. For production use, consider using stronger algorithms like BCrypt or Argon2.

## Future Enhancements

- Tour package booking system
- Hotel reservation management
- Payment gateway integration
- Admin panel for managing destinations
- Customer reviews and ratings
- Booking history and receipts

## License

This project is created for educational purposes.

## Contact

For support or queries, please contact your system administrator.
