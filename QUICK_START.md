# Quick Start Guide

## ✅ Database Setup - COMPLETE
Your database has been successfully initialized with:
- Database: `travel_management_db`
- Table: `users` with all required fields
- MySQL Password: Set in `Database.java`

## 🔧 Next Step: Install Maven

Maven is required to build and run this JavaFX project. 

### Option 1: Install Maven (Recommended)

1. **Download Maven:**
   - Go to: https://maven.apache.org/download.cgi
   - Download `apache-maven-3.9.x-bin.zip`

2. **Install Maven:**
   - Extract to: `C:\Program Files\Apache\maven`
   - Add to PATH: `C:\Program Files\Apache\maven\bin`
   - Restart PowerShell

3. **Verify Installation:**
   ```powershell
   mvn -version
   ```

4. **Build and Run:**
   ```powershell
   mvn clean install
   mvn javafx:run
   ```

### Option 2: Use IntelliJ IDEA (Easiest)

1. **Download IntelliJ IDEA Community Edition:**
   - https://www.jetbrains.com/idea/download/

2. **Open Project:**
   - File → Open → Select `Travel_management_system` folder
   - IntelliJ will auto-detect Maven and download dependencies

3. **Run Application:**
   - Find `Main.java` in Project Explorer
   - Right-click → Run 'Main.main()'

### Option 3: Use Eclipse

1. **Download Eclipse IDE for Java Developers:**
   - https://www.eclipse.org/downloads/

2. **Open Project:**
   - File → Import → Existing Maven Projects
   - Select `Travel_management_system` folder

3. **Run Application:**
   - Right-click on project → Run As → Java Application
   - Select `Main` class

### Option 4: Use VS Code

1. **Install Extensions:**
   - Java Extension Pack
   - Maven for Java

2. **Open Folder:**
   - Open `Travel_management_system` folder

3. **Run:**
   - Press F5 or use Run button

## 📝 What's Already Done

✅ All Java source files created
✅ All FXML UI files created  
✅ Database initialized with tables
✅ MySQL password configured
✅ Project structure complete

## 🎯 You're Almost There!

Just install Maven or use an IDE, and you'll be able to run the Travel Management System!

---

**Need help?** The README.md file has complete documentation.
