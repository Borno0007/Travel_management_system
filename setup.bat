@echo off
echo ========================================
echo Travel Management System - Setup
echo ========================================
echo.

echo Step 1: Checking MySQL connection...
echo Please make sure MySQL is running on your system.
echo.

echo Step 2: Creating database and tables...
echo You will need to run the SQL script manually.
echo.
echo Option 1 - Using MySQL Command Line:
echo   mysql -u root -p ^< sql\init_db.sql
echo.
echo Option 2 - Using MySQL Workbench:
echo   1. Open MySQL Workbench
echo   2. Connect to your local MySQL server
echo   3. File ^> Open SQL Script
echo   4. Select: sql\init_db.sql
echo   5. Execute the script
echo.

pause

echo.
echo Step 3: Building the project...
call mvn clean install

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo To run the application, execute:
    echo   mvn javafx:run
    echo.
    echo Or you can run: run.bat
    echo.
) else (
    echo.
    echo ========================================
    echo Build failed! Please check the errors above.
    echo ========================================
)

pause
