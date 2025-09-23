# AI Collaboration Log

## Session 1: Brainstorming - Domain Selection and Schema Design

### Prompt 1:

"I need some ideas for CRUD style CLI projects for my database class. What are some good domains I could work with and what kind of operations would make sense for each?"

**AI Response:**

```
Here are some CRUD-friendly CLI project ideas:

• Library Management System - manage books, members, borrowing/returning, fines
• Student Information System - handle student records, courses, grades, enrollment
• Inventory Management - track products, suppliers, orders, stock levels
• Hospital Management - patients, doctors, appointments, medical records
• Restaurant Ordering System - menu items, customers, orders, staff scheduling
```

### Prompt 2:

"What if I go with a school system where student ID is the primary key? Can you help me figure out what tables I'd need and what columns should go in each table?"

**AI Response:**

```
For a school system, here's a basic structure:

• Students table: student_id (PK), first_name, last_name, email, phone, address, enrollment_date
• Courses table: course_id (PK), course_name, course_code, credits, department, description
• Instructors table: instructor_id (PK), first_name, last_name, email, department, hire_date
• Enrollments table: enrollment_id (PK), student_id (FK), course_id (FK), semester, year, grade
• Departments table: dept_id (PK), dept_name, dept_head, building, budget
```

### Prompt 3:

"How can I expand this school system idea to meet the requirement of having at least 5 base tables for a real domain?"

**AI Response:**

```
You can expand your school system with these additional tables:

• Assignments table: assignment_id, course_id (FK), title, description, due_date, max_points
• Grades table: grade_id, student_id (FK), assignment_id (FK), points_earned, submission_date
• Classrooms table: room_id, building, room_number, capacity, equipment
• Course_Schedule table: schedule_id, course_id (FK), room_id (FK), instructor_id (FK), time_slot, days
• Prerequisites table: prereq_id, course_id (FK), required_course_id (FK)
```

### Prompt 4:

"Can you clean up these tables and remove any redundant columns? I want to keep only the most important and relevant ones for my project."

**AI Response:**

```
Here's a streamlined version focusing on core functionality:

• Students: student_id (PK), first_name, last_name, email, major
• Courses: course_id (PK), course_name, course_code, credits, department
• Instructors: instructor_id (PK), first_name, last_name, email, department
• Enrollments: student_id (FK), course_id (FK), semester, year, grade
• Assignments: assignment_id (PK), course_id (FK), title, due_date, max_points
```

### What we adopted/changed/rejected:

**Adopted:**

- School management system as our core domain - it's familiar and has clear CRUD operations
- Basic structure with Students, Courses, Instructors, and Enrollments as foundation tables
- Added Assignments table to reach the 5-table minimum requirement
- Kept essential columns that support realistic business operations

**Changed:**

- Simplified the AI's initial complex structure to focus on core relationships
- Removed redundant tables like Prerequisites and Course_Schedule to avoid over-engineering
- Consolidated some attributes (removed address, phone from students to focus on academic data)
- Made Enrollments table use composite key instead of surrogate key for better normalization

**Rejected:**

- Overly complex scheduling system with classrooms and time slots - too complicated for our scope
- Separate Grades table - decided to incorporate grades directly into Enrollments
- Department budget tracking - not relevant to our core student management focus

### What AI got wrong/generic and how we fixed it:

The AI initially suggested very generic database structures without considering our specific use case. The responses were textbook examples that didn't account for:

- Our project's 5-table minimum requirement specifically
- The need for meaningful relationships between entities
- Practical CRUD operations that would make sense in a CLI application

We validated and improved by:

- Researching actual student information systems to understand real-world relationships
- Ensuring each table serves a specific purpose in our domain
- Planning out specific CRUD operations we could implement (add/drop courses, grade assignments, etc.)
- Considering what kind of queries would be interesting for the SQL requirements (joins between students/courses/grades)
