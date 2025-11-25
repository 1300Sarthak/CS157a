# How to Run the School Management System

Complete step-by-step instructions to set up and run the CS157A Final Project.

---

## Prerequisites

Before starting, make sure you have:

- **MySQL Server** running (version 8.0 or higher)
- **Java JDK** installed (version 8 or higher, JDK 11+ recommended)
- **MySQL JDBC Connector** JAR file (`mysql-connector-java-8.0.33.jar`)
- Command line access (Terminal/PowerShell)

---

## Step 1: Set Up MySQL Database

### 1.1 Start MySQL Server

```bash
# macOS/Linux
mysql.server start

# Windows (run as administrator)
net start MySQL80
```

### 1.2 Create and Populate Database

Navigate to the `SchoolSysApp` directory and run the SQL script:

```bash
cd SchoolSysApp
mysql -u root -p < create_and_populate.sql
```

Enter your MySQL root password when prompted.

**What this does:**

- Creates the `SchoolSys` database
- Creates all tables (Student, Course, Enrollment, Instructor, Classroom)
- Adds indexes, triggers, views, and stored procedures
- Populates with sample data

### 1.3 Verify Database Creation

```bash
mysql -u root -p
```

Then in MySQL:

```sql
USE SchoolSys;
SHOW TABLES;
SELECT COUNT(*) FROM Student;
SELECT COUNT(*) FROM Course;
SELECT COUNT(*) FROM Enrollment;
```

You should see 5 tables and record counts.

---

## Step 2: Configure Database Connection

### 2.1 Edit `app.properties`

Open `app.properties` in the `SchoolSysApp` directory:

```bash
# macOS/Linux
nano app.properties
# or
open app.properties

# Windows
notepad app.properties
```

### 2.2 Update Credentials

Change the password line to match your MySQL root password:

```properties
db.url=jdbc:mysql://localhost:3306/SchoolSys?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE
```

Replace `YOUR_MYSQL_PASSWORD_HERE` with your actual MySQL password.

**Important:** Keep this file secure and don't commit passwords to version control!

---

## Step 3: Get MySQL JDBC Connector

### Option A: Copy Existing JAR File

If you already have the connector JAR file:

```bash
cp /path/to/mysql-connector-java-8.0.33.jar SchoolSysApp/
```

### Option B: Download New JAR File

1. Download from: https://dev.mysql.com/downloads/connector/j/
2. Choose "Platform Independent" version
3. Extract and copy `mysql-connector-java-8.0.33.jar` to `SchoolSysApp/` directory

**Verify:** The JAR file should be in the same directory as `app.properties`

---

## Step 4: Compile the Java Application

### 4.1 Navigate to SchoolSysApp Directory

```bash
cd SchoolSysApp
```

### 4.2 Compile

**macOS/Linux:**

```bash
javac -cp ".:mysql-connector-java-8.0.33.jar" src/Main.java
```

**Windows:**

```bash
javac -cp ".;mysql-connector-java-8.0.33.jar" src/Main.java
```

**What happens:**

- Creates `src/Main.class` file
- If compilation succeeds, you're ready to run!

**Common errors:**

- "cannot find symbol" → Check JAR file is in correct location
- "package does not exist" → Verify classpath includes the JAR file

---

## Step 5: Run the Application

### 5.1 Start the Application

**macOS/Linux:**

```bash
java -cp ".:mysql-connector-java-8.0.33.jar:src" Main
```

**Windows:**

```bash
java -cp ".;mysql-connector-java-8.0.33.jar;src" Main
```

### 5.2 Expected Output

You should see:

```
Connecting to database...
Connected!

School Management System
1. View Operations
2. Insert Operations
3. Update Operations
4. Delete Operations
5. Transactional Workflow
9. Exit
Enter choice:
```

---

## Step 6: Using the Application

### Main Menu Options

1. **View Operations** - Browse data (students, courses, enrollments)
2. **Insert Operations** - Add new records
3. **Update Operations** - Modify existing records
4. **Delete Operations** - Remove records
5. **Transactional Workflow** - Multi-course enrollment with COMMIT/ROLLBACK
6. **Exit** - Close the application

### Example Workflow

1. Choose `1` to view operations
2. Select `1` to view all students
3. Press Enter to continue
4. Try inserting a new student (option `2` → `1`)
5. Test the transactional workflow (option `5`)

---

## Testing Checklist

Use this checklist to verify everything works:

### ✅ Basic Functionality

- [ ] **Connection Test**: App starts and shows "Connected!"
- [ ] **View Students**: Menu option 1 → 1 shows student list
- [ ] **View Courses**: Menu option 1 → 4 shows course list
- [ ] **View Enrollments**: Menu option 1 → 8 shows enrollment list

### ✅ CRUD Operations

- [ ] **INSERT**: Add a new student (Menu 2 → 1)
- [ ] **UPDATE**: Update student email (Menu 3 → 1)
- [ ] **DELETE**: Delete a student (Menu 4 → 1)

### ✅ Advanced Features

- [ ] **VIEW**: View Student Transcript (Menu 1 → 11)
- [ ] **Transaction COMMIT**: Enroll student in multiple courses successfully
- [ ] **Transaction ROLLBACK**: Try enrolling in CS157A Fall 2025 (capacity exceeded)

### ✅ Validation

- [ ] **Input Validation**: Try entering invalid data (empty fields, wrong format)
- [ ] **Constraint Test**: Try invalid semester format (e.g., "fall2025")
- [ ] **Error Handling**: See user-friendly error messages

---

## Troubleshooting

### Problem: "Access denied for user"

**Solution:**

- Check `app.properties` credentials match your MySQL setup
- Verify MySQL server is running
- Try connecting manually: `mysql -u root -p`

### Problem: "Unknown database 'SchoolSys'"

**Solution:**

- Run `create_and_populate.sql` first: `mysql -u root -p < create_and_populate.sql`
- Verify database exists: `mysql -u root -p -e "SHOW DATABASES;"`

### Problem: "No suitable driver found"

**Solution:**

- Ensure `mysql-connector-java-8.0.33.jar` is in `SchoolSysApp/` directory
- Check classpath syntax:
  - macOS/Linux: Use `:` (colon)
  - Windows: Use `;` (semicolon)
- Verify JAR file name matches exactly

### Problem: "Cannot find Main"

**Solution:**

- Make sure you're in `SchoolSysApp` directory
- Check that `src/Main.class` exists (compile first)
- Verify classpath includes `:src` (macOS/Linux) or `;src` (Windows)

### Problem: "ClassNotFoundException"

**Solution:**

- Verify JAR file is in correct location
- Check classpath includes the JAR file
- Try absolute path to JAR file if relative path doesn't work

---

## Quick Test Commands

Test database features directly in MySQL:

### Verify Database Setup

```bash
mysql -u root -p -e "USE SchoolSys; SHOW TABLES;"
```

### Test View

```bash
mysql -u root -p -e "USE SchoolSys; SELECT * FROM student_transcript_view LIMIT 5;"
```

### Test Stored Procedure

```bash
mysql -u root -p -e "USE SchoolSys; CALL sp_enroll_student_in_course(1, 1, 'Spring 2027', NULL, @status, @success); SELECT @status, @success;"
```

### Test Trigger (Should Fail)

```bash
mysql -u root -p -e "USE SchoolSys; INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES (3, 1, 'Fall 2025', NULL);"
```

This should fail because CS157A Fall 2025 is at capacity (2 students).

---

## Project Structure

```
SchoolSysApp/
├── src/
│   └── Main.java              # Java console application
├── create_and_populate.sql    # Database setup script
├── app.properties             # Database configuration
├── README.md                  # Project documentation
├── ai_log.md                  # AI collaboration log
├── Team-roles.txt             # Team contributions
└── mysql-connector-java-8.0.33.jar  # JDBC driver (add this)
```

---

## Tips for Success

1. **Start MySQL first** - Always ensure MySQL server is running before starting the app
2. **Check app.properties** - Most connection errors are due to wrong credentials
3. **Test incrementally** - Test database setup, then connection, then features
4. **Read error messages** - They usually tell you exactly what's wrong
5. **Use sample data** - The SQL script includes test data, use it to explore features

---

## Need Help?

- Check `README.md` for detailed project information
- Review `create_and_populate.sql` comments for database structure
- Verify all files are in correct locations
- Test database connection separately before running Java app

---

**Good luck!** 🚀
