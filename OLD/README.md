# CS157A Final Project - School Management System

A complete database application for managing students, courses, and enrollments in a school system. This project demonstrates database design, SQL programming, and Java JDBC integration.

## Project Overview

This application provides a console-based interface for managing a school database system with the following core entities:

- **Students**: Student records with personal information
- **Courses**: Course offerings with instructor and classroom assignments
- **Enrollments**: Student course enrollments with grades and semester tracking
- **Instructors**: Faculty information
- **Classrooms**: Physical classroom locations with capacity limits

## Environment Requirements

### Software Versions

- **MySQL**: 8.0 or higher
- **Java JDK**: 8 or higher (JDK 11+ recommended)
- **MySQL JDBC Connector**: mysql-connector-java-8.0.x.jar (included as `mysql-connector-java-8.0.33.jar`)

### System Requirements

- MySQL server running and accessible
- Command line access (Terminal on macOS/Linux, Command Prompt or PowerShell on Windows)
- Java compiler (`javac`) and runtime (`java`) in PATH

## Quick Start Guide

### Step 1: Database Setup

1. **Start MySQL server** (if not already running)

   ```bash
   # macOS/Linux
   mysql.server start

   # Windows (run as administrator)
   net start MySQL80
   ```

2. **Create and populate the database**

   ```bash
   mysql -u root -p < create_and_populate.sql
   ```

   Enter your MySQL root password when prompted.

3. **Verify database creation**
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

### Step 2: Configure Application

1. **Edit `app.properties`** with your MySQL credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/SchoolSys?useSSL=false&serverTimezone=UTC
   db.username=root
   db.password=your_password_here
   ```
   Replace `your_password_here` with your actual MySQL root password.

### Step 3: Compile and Run

1. **Compile the Java application**

   ```bash
   # macOS/Linux
   javac -cp ".:mysql-connector-java-8.0.33.jar" Main.java

   # Windows
   javac -cp ".;mysql-connector-java-8.0.33.jar" Main.java
   ```

2. **Run the application**

   ```bash
   # macOS/Linux
   java -cp ".:mysql-connector-java-8.0.33.jar" Main

   # Windows
   java -cp ".;mysql-connector-java-8.0.33.jar" Main
   ```

3. **Navigate the menu** using the numbered options

## Project Structure

```
CS157a/
├── Main.java                    # Java console application
├── create_and_populate.sql      # Complete SQL script (schema + data + view + procedure + constraints)
├── app.properties               # Database configuration
├── mysql-connector-java-8.0.33.jar  # MySQL JDBC driver
├── README.md                    # This file
├── ai_log.md                    # AI collaboration record
├── Team-roles.txt              # Team member contributions
└── video_demo.mp4              # Demo video (to be recorded)
```

## Requirements Implementation Checklist

### Part 1: Domain + E/R Design ✅

- **Deliverable**: E/R diagram PDF
- **Status**: Completed in Phase A
- **Domain**: School Management System
- **Entities**: Student, Course, Instructor, Classroom, Enrollment (5+ tables)

### Part 2: Relational Schema ✅

- **Deliverable**: Schema mapping PDF
- **Status**: Completed in Phase A
- **File**: Schema documented in `create_and_populate.sql`
- **Tables**: 5 base tables with proper PKs, FKs, and constraints

### Part 3: MySQL Implementation & Population ✅

- **Deliverable**: SQL scripts
- **Status**: Completed in Phase B
- **File**: `create_and_populate.sql`
- **Content**: CREATE TABLE statements, INSERT statements for sample data

### Part 4: Queries, Updates, Indexes, Transactions ✅

- **Deliverable**: Query suite
- **Status**: Completed in Phase B
- **Features**:
  - Multi-table JOINs (implemented in Java app)
  - Subqueries (used in INSERT statements)
  - Aggregates (GROUP BY in queries)
  - Indexes: `idx_enroll_student_sem`, `idx_enroll_course_sem` (defined in `create_and_populate.sql`)
  - Transactions: Demonstrated in Java app

### Part 5: Java Console App with JDBC ✅

- **Deliverable**: `Main.java`
- **Status**: Completed in Phase C
- **Features**:
  - Menu-driven console interface
  - All SQL uses PreparedStatement (no string concatenation)
  - **Transactional workflow**: `enrollStudentInMultipleCourses()` method in `Main.java` (lines ~359-520)
  - Input validation with helpful error messages
  - Configuration via `app.properties`
  - **Location**: `Main.java` - `handleTransactionalWorkflow()` → `enrollStudentInMultipleCourses()`

### Part 6: Views, Constraints, Triggers ✅

- **Deliverable**: SQL enhancements
- **Status**: Completed in Phase C
- **Features**:
  - **VIEW**: `student_transcript_view` (defined in `create_and_populate.sql` around line 150)
    - Purpose: Convenient transcript reporting combining Student, Enrollment, Course, and Instructor data
    - Used in: Java app menu option "View Student Transcript"
  - **STORED PROCEDURE**: `sp_enroll_student_in_course` (defined in `create_and_populate.sql` around line 160)
    - Purpose: Automates enrollment with validation (duplicate check, capacity check, grade validation)
    - Parameters: IN (student_id, course_id, semester, grade), OUT (status, success)
  - **TRIGGER**: `trg_enrollment_capacity` (defined in `create_and_populate.sql` around line 120)
    - Purpose: Prevents over-enrollment by checking classroom capacity before INSERT
    - Fires: BEFORE INSERT on Enrollment table
  - **CONSTRAINTS**:
    - CHECK constraint on `Enrollment.semester` format (defined in `create_and_populate.sql` around line 100)
    - CHECK constraint on `Enrollment.grade` values (defined in table creation)
    - CHECK constraint on `Course.credits` (1-6 range)
    - CHECK constraint on `Classroom.capacity` (> 0)
  - **INDEXES**: `idx_enroll_student_sem`, `idx_enroll_course_sem` (defined in `create_and_populate.sql` around line 110)

### Part 7: Wrap-up & Reproducibility ✅

- **Deliverable**: Documentation and final package
- **Status**: Completed in Phase C
- **Files**: This README, `create_and_populate.sql`, `app.properties`, `Main.java`
- **Reproducibility**: One-command database rebuild, properties-driven config, clear build instructions

## Key Features Demonstrated

### Database Features

- **Views**: `student_transcript_view` for convenient transcript queries
- **Stored Procedures**: `sp_enroll_student_in_course` for automated enrollment validation
- **Triggers**: Capacity check trigger prevents over-enrollment
- **Constraints**: Semester format, grade values, credits range, capacity validation
- **Indexes**: Optimized queries on enrollment lookups

### Java Application Features

- **PreparedStatement**: All SQL operations use parameterized queries
- **Transactional Workflow**: Multi-course enrollment with COMMIT/ROLLBACK
- **Input Validation**: Client-side validation with helpful error messages
- **Error Handling**: User-friendly error messages for constraint violations
- **Menu System**: Intuitive console menu for all operations

## Testing the Application

### Testing the VIEW

```sql
-- Query the student transcript view
SELECT * FROM student_transcript_view WHERE email = 'sarah.lee@sjsu.edu';
```

### Testing the STORED PROCEDURE

```sql
-- Call the enrollment procedure
SET @status = '';
SET @status = '';
SET @success = FALSE;
CALL sp_enroll_student_in_course(
    (SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
    (SELECT course_id FROM Course WHERE course_code = 'MATH161'),
    'Spring 2027',
    NULL,
    @status,
    @success
);
SELECT @status AS result_status, @success AS success_flag;
```

### Testing the TRIGGER

```sql
-- This should fail (capacity exceeded)
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
 'Fall 2025', NULL);
```

### Testing Constraints

```sql
-- This should fail (invalid semester format)
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
 'fall2025', NULL);
```

## Video Demo Suggestions

For your ≤ 6 minute video demonstration, consider showing features in this order:

1. **Menu Navigation** (30 seconds)

   - Show main menu and sub-menus
   - Demonstrate menu structure

2. **VIEW Query** (30 seconds)

   - Use "View Student Transcript" menu option
   - Show how `student_transcript_view` simplifies complex JOINs

3. **INSERT Operation** (30 seconds)

   - Add a new student or enroll a student in a course
   - Show input validation

4. **UPDATE Operation** (30 seconds)

   - Update a student's email or course credits
   - Show constraint validation

5. **DELETE Operation** (30 seconds)

   - Delete a student or drop an enrollment
   - Show CASCADE behavior

6. **Transaction Success (COMMIT)** (1.5 minutes)

   - Use "Transactional Workflow" menu option
   - Enroll student in multiple courses successfully
   - Show COMMIT message clearly

7. **Transaction Failure (ROLLBACK)** (1.5 minutes)

   - Use "Transactional Workflow" again
   - Intentionally cause failure (capacity exceeded)
   - Show ROLLBACK message clearly
   - Emphasize atomicity (all or nothing)

8. **Stored Routine Test** (30 seconds)
   - Show how to call `sp_enroll_student_in_course` from MySQL
   - Or demonstrate it's used in the Java app

## Troubleshooting

### Connection Errors

- **Error**: "Access denied for user"

  - **Solution**: Check `app.properties` credentials match your MySQL setup

- **Error**: "Unknown database 'SchoolSys'"
  - **Solution**: Run `create_and_populate.sql` first to create the database

### Compilation Errors

- **Error**: "cannot find symbol: mysql-connector-java"
  - **Solution**: Ensure `mysql-connector-java-8.0.33.jar` is in the same directory as `Main.java`

### Runtime Errors

- **Error**: "No suitable driver found"
  - **Solution**: Check classpath includes the JAR file (use `:` on macOS/Linux, `;` on Windows)

## Limitations & Future Work

### Current Limitations

- Console-based interface (no GUI)
- Single-user application (no concurrent access handling)
- Basic error handling (could be enhanced with logging)
- No authentication/authorization system

### Future Enhancements

- Web-based interface using Java Servlets/JSP
- User authentication and role-based access control
- Advanced reporting and analytics
- Email notifications for enrollment confirmations
- Integration with external student information systems
- Batch enrollment processing
- Grade calculation and GPA tracking

## Team Information

See `Team-roles.txt` for individual team member contributions and reflections.

## AI Collaboration

See `ai_log.md` for documentation of AI-assisted development sessions.

---

_This project was completed as part of CS157A Database Management Systems course._
