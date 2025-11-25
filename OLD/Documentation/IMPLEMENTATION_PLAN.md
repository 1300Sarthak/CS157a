# Implementation Plan for Phase C - Parts 5, 6, 7

## Current Repository Analysis

### Existing SQL Files

- **None found** - Schema is documented in `schema.md` and `script.md`, but no `.sql` files exist yet (the code is sql.md its just not in an SQL file format currently though)
- Need to create: `create_and_populate.sql` (consolidated schema + data + views + routines)

### Existing Java Files

- **None found** - No Java code exists yet
- Need to create: `Main.java` (console menu-driven JDBC application)

### Schema Summary

**Main Tables:**

1. **Student** (PK: student_id)

   - Attributes: first_name, last_name, dob, email (UNIQUE)
   - Relationships: Many-to-many with Course via Enrollment

2. **Course** (PK: course_id)

   - Attributes: course_name, course_code (UNIQUE), credits (CHECK 1-6), instructor_id (FK), classroom_id (FK)
   - Relationships: FK to Instructor, FK to Classroom, Many-to-many with Student via Enrollment

3. **Instructor** (PK: instructor_id)

   - Attributes: first_name, last_name, email (UNIQUE), department
   - Relationships: One-to-many with Course

4. **Classroom** (PK: classroom_id)

   - Attributes: building, room_number, capacity (CHECK > 0)
   - Relationships: One-to-many with Course

5. **Enrollment** (Composite PK: student_id, course_id, semester)
   - Attributes: student_id (FK), course_id (FK), semester, grade (CHECK or NULL)
   - Relationships: FK to Student, FK to Course

**Existing Features:**

- ✅ Trigger: `trg_enrollment_capacity` (prevents over-enrollment)
- ✅ Indexes: `idx_enroll_student_sem`, `idx_enroll_course_sem`
- ✅ Constraints: CHECK on capacity > 0, credits BETWEEN 1-6, grade values

---

## Chosen Tables for Console App

**Selected: Student, Course, Enrollment** (3 core tables)

**Rationale:**

- These tables form the core workflow: students enroll in courses
- Enrollment is the natural transactional hub (multi-table operations)
- Student and Course are independent entities that users need to view/manage
- Instructor and Classroom are supporting tables (can be accessed via Course relationships)

---

## Menu Options Per Table

### 1. STUDENT Table

**SELECT Features:**

- **View All Students** - List all students with ID, name, email, DOB
- **View Student by Email** - Search and display a specific student's details
- **View Student Enrollments** - Show all courses a student is enrolled in (JOIN with Enrollment and Course)

**INSERT Feature:**

- **Add New Student** - Create a new student record with validation:
  - Email must be unique (catch UNIQUE constraint violation)
  - First/last name cannot be empty
  - DOB must be valid date format

**UPDATE Feature:**

- **Update Student Email** - Change a student's email (with uniqueness check)

**DELETE Feature:**

- **Delete Student** - Remove a student (CASCADE will delete enrollments automatically)

---

### 2. COURSE Table

**SELECT Features:**

- **View All Courses** - List all courses with code, name, credits, instructor name, classroom location
- **View Course by Code** - Search course by course_code (e.g., "CS157A")
- **View Course Roster** - Show all students enrolled in a specific course for a given semester (JOIN with Enrollment and Student)
- **View Courses by Instructor** - List all courses taught by a specific instructor

**INSERT Feature:**

- **Add New Course** - Create a new course with validation:
  - Course code must be unique
  - Credits must be between 1-6
  - Instructor ID and Classroom ID must exist (FK validation)

**UPDATE Feature:**

- **Update Course Credits** - Modify credits for an existing course (with CHECK constraint validation)

**DELETE Feature:**

- **Delete Course** - Remove a course (will CASCADE delete enrollments, RESTRICT if instructor/classroom dependencies exist)

---

### 3. ENROLLMENT Table

**SELECT Features:**

- **View All Enrollments** - List all enrollments with student name, course code, semester, grade
- **View Enrollments by Semester** - Filter enrollments for a specific semester
- **View Enrollments by Course** - Show all students in a specific course for a semester
- **View Student Transcript** - Display all courses and grades for a specific student (ordered by semester)

**INSERT Feature:**

- **Enroll Student in Course** - Add a new enrollment with validation:
  - Student ID and Course ID must exist
  - Cannot duplicate enrollment (same student + course + semester)
  - Will trigger capacity check (existing trigger)
  - Grade is optional (NULL for current enrollments)

**UPDATE Feature:**

- **Update Grade** - Assign or modify a grade for an enrollment:
  - Grade must be valid ('A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'C-', 'D', 'F')
  - Enrollment must exist

**DELETE Feature:**

- **Drop Enrollment** - Remove a student from a course (for a specific semester)

---

## Transactional Workflow Design

### Workflow: "Enroll Student in Multiple Courses for a Semester"

**Purpose:** Demonstrates multi-table transaction with COMMIT/ROLLBACK

**Steps:**

1. User selects: "Enroll student in multiple courses"
2. Prompt for: student_id (or email lookup), semester, list of course_ids (or course codes)
3. **START TRANSACTION**
4. For each course:
   - Validate student exists
   - Validate course exists
   - Check if enrollment already exists (duplicate check)
   - Check classroom capacity (via existing trigger)
   - INSERT into Enrollment
5. If ANY step fails:
   - **ROLLBACK** entire transaction
   - Display clear error message (which course failed and why)
6. If ALL succeed:
   - **COMMIT** transaction
   - Display success message with all enrollments created

**Error Scenarios to Handle:**

- Student doesn't exist → ROLLBACK
- Course doesn't exist → ROLLBACK
- Duplicate enrollment → ROLLBACK
- Capacity exceeded (trigger fires) → ROLLBACK
- Invalid grade format → ROLLBACK

**Example Flow:**

```
User: Enroll student "sarah.lee@sjsu.edu" in courses ["CS157A", "MATH161"] for "Fall 2025"
System: Validating...
System: [If CS157A capacity exceeded] Error: Cannot enroll in CS157A - capacity exceeded. Rolling back all enrollments.
System: [If all succeed] Successfully enrolled in 2 courses. Transaction committed.
```

---

## View Definition Idea

### View: `student_transcript_view`

**Purpose:** Convenient reporting view that combines student, course, and enrollment data for transcript-like display

**Definition:**

```sql
CREATE VIEW student_transcript_view AS
SELECT
    s.student_id,
    s.first_name,
    s.last_name,
    s.email,
    e.semester,
    c.course_code,
    c.course_name,
    c.credits,
    e.grade,
    i.first_name AS instructor_first_name,
    i.last_name AS instructor_last_name
FROM Student s
JOIN Enrollment e ON s.student_id = e.student_id
JOIN Course c ON e.course_id = c.course_id
JOIN Instructor i ON c.instructor_id = i.instructor_id
ORDER BY s.student_id, e.semester, c.course_code;
```

**Use Cases:**

- Quick transcript lookup without writing JOINs each time
- Used in Java app: "View Student Transcript" menu option
- Can filter by student_id or email in WHERE clause

**Java Integration:**

- Menu option: "View Student Transcript" will query this view with WHERE student_id = ?

---

## Stored Procedure or Function Idea

### Stored Procedure: `sp_enroll_student_in_course`

**Purpose:** Automates the multi-step enrollment process with business rule enforcement

**Functionality:**

1. Validates student exists
2. Validates course exists
3. Checks for duplicate enrollment (same student + course + semester)
4. Checks classroom capacity (before insert, to provide better error message)
5. Inserts enrollment if all checks pass
6. Returns success/error status via OUT parameter

**Parameters:**

- IN: p_student_id INT
- IN: p_course_id INT
- IN: p_semester VARCHAR(20)
- IN: p_grade CHAR(2) (optional, defaults to NULL)
- OUT: p_status VARCHAR(100) (success message or error description)
- OUT: p_success BOOLEAN

**Business Rules Enforced:**

- Prevents duplicate enrollments
- Validates capacity before attempting insert (better error handling than trigger alone)
- Validates grade format if provided

**Java Integration:**

- Can be called from Java PreparedStatement for "Enroll Student" menu option
- Provides cleaner error handling than multiple separate queries
- Demonstrates stored routine usage requirement

**Alternative Consideration:** Could be a FUNCTION that returns enrollment count for a course/semester, but PROCEDURE is better for multi-step validation workflow.

---

## Constraint Design

### New Constraint: CHECK constraint on Enrollment.semester format

**Current State:** Semester is VARCHAR(20) with no format validation

**Proposed Constraint:**

```sql
ALTER TABLE Enrollment
ADD CONSTRAINT chk_semester_format
CHECK (semester REGEXP '^(Fall|Spring|Summer|Winter) [0-9]{4}$');
```

**Purpose:**

- Enforces consistent semester format: "Fall 2025", "Spring 2026", etc.
- Prevents typos and inconsistent data entry
- Makes queries and reports more reliable

**Validation:**

- Must start with: Fall, Spring, Summer, or Winter
- Followed by space
- Followed by exactly 4 digits (year)

**Error Handling:**

- Java app will catch CHECK constraint violation
- Display helpful message: "Semester must be in format 'Fall YYYY', 'Spring YYYY', etc."

**Note:** This constraint will be added to `create_and_populate.sql`. Existing data already follows this format, so no migration needed.

---

## Required Files and Structure for Final Submission

### File Structure:

```
CS157a/
├── Main.java                    # Java console application
├── create_and_populate.sql      # Complete SQL script (schema + data + view + procedure + constraints)
├── app.properties               # Database configuration
├── README.md                    # Build, run, and environment instructions
├── ai_log.md                    # AI collaboration record (already exists)
├── Team-roles.txt              # Team member contributions and reflections
└── video_demo.mp4              # Demo video (user will record)
```

### Main.java Structure:

```
- Package declaration (if any)
- Imports (java.sql.*, java.util.*, java.io.*)
- Main class with:
  - Connection management methods (loadProperties, getConnection, closeConnection)
  - Menu display methods (printMainMenu, printStudentMenu, etc.)
  - Student operations (viewAllStudents, addStudent, updateStudentEmail, deleteStudent, viewStudentEnrollments)
  - Course operations (viewAllCourses, addCourse, updateCourseCredits, deleteCourse, viewCourseRoster, viewCoursesByInstructor)
  - Enrollment operations (viewAllEnrollments, enrollStudent, updateGrade, dropEnrollment, viewTranscript)
  - Transactional workflow method (enrollStudentInMultipleCourses)
  - Input validation helpers (validateEmail, validateDate, validateGrade, etc.)
  - Error handling with try-catch blocks
  - All SQL via PreparedStatement
```

### create_and_populate.sql Structure:

```
1. DROP statements (in FK-safe order)
2. CREATE TABLE statements (Student, Instructor, Classroom, Course, Enrollment)
3. CREATE INDEX statements
4. CREATE TRIGGER statements (existing capacity trigger)
5. ALTER TABLE for new constraint (semester format)
6. CREATE VIEW statement (student_transcript_view)
7. CREATE PROCEDURE statement (sp_enroll_student_in_course)
8. INSERT statements for sample data (Instructor, Classroom, Course, Student, Enrollment)
9. Test queries (optional, for verification)
```

### app.properties Structure:

```
db.url=jdbc:mysql://localhost:3306/SchoolSys?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password_here
```

### README.md Structure:

```
# CS157A Final Project - School Management System

## Environment Requirements
- Java JDK 8 or higher
- MySQL 8.0 or higher
- MySQL JDBC Connector (mysql-connector-java-8.0.x.jar)

## Database Setup
1. Start MySQL server
2. Run: `mysql -u root -p < create_and_populate.sql`
3. Verify database: `USE SchoolSys; SHOW TABLES;`

## Build Instructions
1. Compile: `javac -cp ".:mysql-connector-java-8.0.x.jar" Main.java`
2. Run: `java -cp ".:mysql-connector-java-8.0.x.jar" Main`

## Configuration
Edit `app.properties` with your MySQL credentials

## Features Demonstrated
- PreparedStatement usage
- Transactional workflow (multi-course enrollment)
- Input validation
- Error handling
- Views and stored procedures
- Constraints

## Testing the Transactional Workflow
[Instructions on how to test COMMIT/ROLLBACK scenarios]
```

---

## Implementation Order

1. **Create `create_and_populate.sql`** - Consolidate schema, add view, procedure, constraint
2. **Create `app.properties`** - Database configuration template
3. **Create `Main.java`** - Implement menu system and all CRUD operations
4. **Test transactional workflow** - Verify COMMIT/ROLLBACK behavior
5. **Create `README.md`** - Build and run instructions
6. **Update `ai_log.md`** - Document this planning session
7. **Create `Team-roles.txt`** - Team contributions (user will fill)

---

## Key Design Decisions

1. **Menu-Driven Console:** Simple numbered menu system (1-9 for main options, sub-menus for each table)
2. **Error Messages:** All errors caught and displayed with helpful context (which operation failed, why)
3. **Input Validation:** Client-side validation before SQL execution (catch format errors early)
4. **Transaction Scope:** Multi-course enrollment is the primary transaction demo (touches Student, Course, Enrollment)
5. **View Usage:** Transcript view used in "View Student Transcript" menu option
6. **Procedure Usage:** Enrollment procedure called from "Enroll Student" menu option (alternative to direct INSERT)
7. **Constraint Demo:** Semester format constraint will be tested when user enters invalid format

---

## Testing Scenarios to Document

1. **Successful Transaction:** Enroll student in 2 courses → COMMIT → verify both enrollments exist
2. **Failed Transaction:** Enroll student in 2 courses, second exceeds capacity → ROLLBACK → verify no enrollments created
3. **Constraint Violation:** Try to insert enrollment with invalid semester format → catch CHECK constraint error
4. **View Query:** Query student_transcript_view for a student → verify correct JOIN results
5. **Procedure Call:** Call sp_enroll_student_in_course → verify validation and enrollment
6. **Input Validation:** Try invalid email format, invalid grade, etc. → verify helpful error messages

---

This plan is detailed enough to proceed with implementation. All requirements are addressed:

- ✅ 3 tables chosen (Student, Course, Enrollment)
- ✅ SELECT options for each table
- ✅ INSERT/UPDATE/DELETE where appropriate
- ✅ Transactional workflow designed
- ✅ VIEW defined
- ✅ Stored procedure designed
- ✅ Constraint designed
- ✅ File structure defined
