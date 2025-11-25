# Test Cases for School Management System

## Prerequisites
- Database `SchoolSys` must exist and be populated with sample data
- MySQL server must be running
- `app.properties` must be configured with correct credentials

## Test Cases

### 1. VIEW OPERATIONS - Student Table

#### Test 1.1: View All Students
**Steps:**
1. Run application
2. Select option `1` (View Operations)
3. Select option `1` (View All Students)
**Expected:** List of all students with ID, name, email, DOB displayed in table format

#### Test 1.2: View Student by Email
**Steps:**
1. View Operations → `2` (View Student by Email)
2. Enter: `sarah.lee@sjsu.edu`
**Expected:** Student details displayed (ID, name, email, DOB)

**Error Test:** Enter invalid email → Should show "Student not found"

#### Test 1.3: View Student Enrollments
**Steps:**
1. View Operations → `3` (View Student Enrollments)
2. Enter: `sarah.lee@sjsu.edu`
**Expected:** List of all courses student is enrolled in with semester and grade

**Complex Query Test:** This demonstrates a multi-table JOIN (Student → Enrollment → Course)

---

### 2. VIEW OPERATIONS - Course Table

#### Test 2.1: View All Courses
**Steps:**
1. View Operations → `4` (View All Courses)
**Expected:** List of all courses with instructor name and classroom location

**Complex Query Test:** This demonstrates multi-table JOINs (Course → Instructor, Course → Classroom)

#### Test 2.2: View Course by Code
**Steps:**
1. View Operations → `5` (View Course by Code)
2. Enter: `CS157A`
**Expected:** Complete course details including instructor and classroom info

#### Test 2.3: View Course Roster
**Steps:**
1. View Operations → `6` (View Course Roster)
2. Enter course code: `CS157A`
3. Enter semester: `Fall 2025`
**Expected:** List of all students enrolled in the course for that semester

**Complex Query Test:** Multi-table JOIN (Course → Enrollment → Student)

#### Test 2.4: View Courses by Instructor
**Steps:**
1. View Operations → `7` (View Courses by Instructor)
2. Enter instructor email: `jsmith@sjsu.edu`
**Expected:** List of all courses taught by that instructor

---

### 3. VIEW OPERATIONS - Enrollment Table

#### Test 3.1: View All Enrollments
**Steps:**
1. View Operations → `8` (View All Enrollments)
**Expected:** Complete list of all enrollments with student name, course, semester, grade

#### Test 3.2: View Enrollments by Semester
**Steps:**
1. View Operations → `9` (View Enrollments by Semester)
2. Enter: `Fall 2025`
**Expected:** Filtered list of enrollments for that semester

**Validation Test:** Enter invalid format like `fall2025` → Should prompt for correct format

#### Test 3.3: View Enrollments by Course
**Steps:**
1. View Operations → `10` (View Enrollments by Course)
2. Enter: `CS157A`
**Expected:** All enrollments for that course across all semesters

#### Test 3.4: View Student Transcript (Using VIEW)
**Steps:**
1. View Operations → `11` (View Student Transcript)
2. Enter: `sarah.lee@sjsu.edu`
**Expected:** Complete transcript using `student_transcript_view` with all courses, grades, credits, instructors

**Complex Query Test:** This queries a VIEW which demonstrates a complex multi-table JOIN

---

### 4. INSERT OPERATIONS

#### Test 4.1: Add New Student
**Steps:**
1. Insert Operations → `1` (Add New Student)
2. Enter first name: `John`
3. Enter last name: `Doe`
4. Enter email: `john.doe@sjsu.edu`
5. Enter DOB: `2004-05-15` (or press Enter to skip)
**Expected:** "Student added successfully! (1 row inserted)"

**Validation Tests:**
- Empty first name → Should prompt again
- Empty email → Should prompt again
- Invalid date format → Should prompt again
- Duplicate email → Should show "Email already exists" error

#### Test 4.2: Add New Course
**Steps:**
1. Insert Operations → `2` (Add New Course)
2. Review displayed instructor and classroom lists
3. Enter course name: `Data Structures`
4. Enter course code: `CS146`
5. Enter credits: `3`
6. Enter instructor ID: `1` (use ID from displayed list)
7. Enter classroom ID: `1` (use ID from displayed list)
**Expected:** "Course added successfully! (1 row inserted)"

**Validation Tests:**
- Invalid credits (< 1 or > 6) → Should prompt again
- Invalid instructor ID → Should show "Instructor ID does not exist"
- Invalid classroom ID → Should show "Classroom ID does not exist"
- Duplicate course code → Should show "Course code already exists"

#### Test 4.3: Enroll Student in Course
**Steps:**
1. Insert Operations → `3` (Enroll Student in Course)
2. Enter student email: `sarah.lee@sjsu.edu`
3. Enter course code: `CS149`
4. Enter semester: `Fall 2026`
5. Enter grade: (press Enter to leave blank)
**Expected:** "Student enrolled successfully! (1 row inserted)"

**Validation Tests:**
- Invalid student email → Should show "Student not found"
- Invalid course code → Should show "Course not found"
- Invalid semester format → Should prompt for correct format
- Invalid grade format → Should prompt for valid grade
- Duplicate enrollment → Should show "already enrolled" error
- Capacity exceeded → Should show trigger error message

---

### 5. UPDATE OPERATIONS

#### Test 5.1: Update Student Email
**Steps:**
1. Update Operations → `1` (Update Student Email)
2. Enter current email: `sarah.lee@sjsu.edu`
3. Enter new email: `sarah.lee.new@sjsu.edu`
**Expected:** "Email updated successfully! (1 row(s) updated)"

**Validation Tests:**
- Student not found → Should show "Student not found"
- Duplicate new email → Should show "New email already exists"

#### Test 5.2: Update Course Credits
**Steps:**
1. Update Operations → `2` (Update Course Credits)
2. Enter course code: `CS157A`
3. Enter new credits: `4`
**Expected:** "Credits updated successfully! (1 row(s) updated)"

**Validation Tests:**
- Invalid credits → Should prompt again (must be 1-6)
- Course not found → Should show "Course not found"
- Invalid integer input → Should prompt again

#### Test 5.3: Update Grade
**Steps:**
1. Update Operations → `3` (Update Grade)
2. Enter student email: `sarah.lee@sjsu.edu`
3. Enter course code: `CS149`
4. Enter semester: `Spring 2026`
5. Enter grade: `A`
**Expected:** "Grade updated successfully! (1 row(s) updated)"

**Validation Tests:**
- Invalid grade format → Should prompt for valid grade
- Enrollment not found → Should show "Enrollment not found"

---

### 6. DELETE OPERATIONS

#### Test 6.1: Delete Student
**Steps:**
1. Delete Operations → `1` (Delete Student)
2. Enter email: `luis.garcia@sjsu.edu`
3. Confirm: `yes`
**Expected:** "Student deleted successfully! (1 row(s) deleted)" + note about CASCADE

**Validation Tests:**
- Student not found → Should show "Student not found"
- Cancel deletion → Should show "Deletion cancelled"

#### Test 6.2: Delete Course
**Steps:**
1. Delete Operations → `2` (Delete Course)
2. Enter course code: `MATH129A`
3. Confirm: `yes`
**Expected:** "Course deleted successfully! (1 row(s) deleted)" + note about CASCADE

**Validation Tests:**
- Course not found → Should show "Course not found"
- Foreign key constraint → Should show appropriate error

#### Test 6.3: Drop Enrollment
**Steps:**
1. Delete Operations → `3` (Drop Enrollment)
2. Enter student email: `ethan.park@sjsu.edu`
3. Enter course code: `MATH161`
4. Enter semester: `Fall 2026`
**Expected:** "Enrollment dropped successfully! (1 row(s) deleted)"

**Validation Tests:**
- Enrollment not found → Should show "Enrollment not found"

---

### 7. TRANSACTIONAL WORKFLOW (COMMIT/ROLLBACK)

#### Test 7.1: Successful Multi-Course Enrollment (COMMIT)
**Steps:**
1. Main Menu → `5` (Transactional Workflow)
2. Enter student email: `aisha.khan@sjsu.edu`
3. Enter semester: `Spring 2027`
4. Enter course codes: `CS157A,MATH161`
**Expected:** 
- "Transaction COMMITTED successfully!"
- List of successfully enrolled courses
- Both enrollments should appear in database

**Verify:** Check database that both enrollments exist

#### Test 7.2: Failed Multi-Course Enrollment (ROLLBACK)
**Steps:**
1. Main Menu → `5` (Transactional Workflow)
2. Enter student email: `sarah.lee@sjsu.edu`
3. Enter semester: `Fall 2025`
4. Enter course codes: `CS157A,MATH161`
**Note:** CS157A already has 2 students enrolled (capacity = 2), so adding a third will fail
**Expected:**
- "Transaction ROLLED BACK - No enrollments were created"
- List of failed enrollments with reasons
- No enrollments should be created in database

**Verify:** Check database that NO enrollments were created for this transaction

#### Test 7.3: Partial Failure Scenario (ROLLBACK)
**Steps:**
1. Main Menu → `5` (Transactional Workflow)
2. Enter student email: `priya.nair@sjsu.edu`
3. Enter semester: `Spring 2027`
4. Enter course codes: `CS157A,INVALID123`
**Expected:**
- "Transaction ROLLED BACK"
- Both enrollments should fail (one due to invalid course, one due to rollback)
- No enrollments created

**Verify:** Check database - even CS157A enrollment should NOT exist

---

## Error Handling Tests

### Constraint Violation Tests

1. **Unique Constraint (Email):**
   - Try adding student with existing email → Should show "Email already exists"

2. **Unique Constraint (Course Code):**
   - Try adding course with existing code → Should show "Course code already exists"

3. **Check Constraint (Credits):**
   - Try updating credits to 7 → Should show constraint violation error

4. **Check Constraint (Semester Format):**
   - Try entering semester as "fall2025" → Should prompt for correct format

5. **Check Constraint (Grade):**
   - Try entering invalid grade → Should prompt for valid grade

6. **Foreign Key Constraint:**
   - Try adding course with invalid instructor ID → Should show "Instructor ID does not exist"
   - Try adding course with invalid classroom ID → Should show "Classroom ID does not exist"

7. **Trigger Constraint (Capacity):**
   - Try enrolling student when classroom is at capacity → Should show trigger error message

---

## Input Validation Tests

### Numeric Input Validation
- Enter non-numeric for credits → Should prompt again
- Enter non-numeric for IDs → Should prompt again
- Enter out-of-range credits (< 1 or > 6) → Should prompt again

### String Input Validation
- Enter empty string for required fields → Should prompt again
- Email validation (handled by database UNIQUE constraint)

### Date Input Validation
- Enter invalid date format → Should prompt again
- Enter date in wrong format → Should show format example

### Semester Format Validation
- Enter invalid semester format → Should show format example and prompt again

---

## Complex Query Verification

### Multi-Table JOINs
- View All Courses: Verifies Course → Instructor JOIN and Course → Classroom JOIN
- View Course Roster: Verifies Course → Enrollment → Student JOIN
- View Student Enrollments: Verifies Student → Enrollment → Course JOIN

### VIEW Query
- View Student Transcript: Verifies querying `student_transcript_view` which contains complex JOINs

### Aggregated Results
- View Course Roster: Shows COUNT of students per course/semester (implicitly)

---

## Transaction Testing Checklist

- [ ] Test successful COMMIT (all enrollments succeed)
- [ ] Test ROLLBACK on capacity exceeded
- [ ] Test ROLLBACK on invalid course code
- [ ] Test ROLLBACK on duplicate enrollment
- [ ] Verify no partial enrollments after ROLLBACK
- [ ] Verify all enrollments exist after COMMIT

---

## Notes

- All operations use PreparedStatement (no string concatenation)
- All error messages are user-friendly
- Input validation loops until valid input is provided
- Foreign key validation shows available options before input
- Transaction demonstrates atomicity (all or nothing)

