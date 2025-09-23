# E/R Diagram - School Management System

## Entity Sets

### STUDENT

- **student_id** (Primary Key)
- first_name
- last_name
- dob
- email (Unique)

### COURSE

- **course_id** (Primary Key)
- course_name
- course_code (Unique)
- credits

### INSTRUCTOR

- **instructor_id** (Primary Key)
- first_name
- last_name
- email (Unique)
- department

### CLASSROOM

- **classroom_id** (Primary Key)
- building
- room_number
- capacity

## Relationships

### ENROLLS_IN

- **Entities**: STUDENT, COURSE
- **Cardinality**: M:N (Many students can enroll in many courses)
- **Attributes**:
  - semester
  - year
  - grade

### TEACHES

- **Entities**: INSTRUCTOR, COURSE
- **Cardinality**: 1:N (One instructor teaches many courses, each course taught by one instructor)
- **Attributes**: None

### SCHEDULED_IN

- **Entities**: COURSE, CLASSROOM
- **Cardinality**: N:1 (Many courses can be scheduled in one classroom, but each course instance uses one classroom)
- **Attributes**:
  - time_slot
  - day_of_week

## Participation Constraints

- STUDENT **totally participates** in ENROLLS_IN (every student must be enrolled in at least one course)
- COURSE **totally participates** in TEACHES (every course must have an instructor)
- COURSE **partially participates** in SCHEDULED_IN (courses may not yet be scheduled)
- INSTRUCTOR **partially participates** in TEACHES (instructors may not be teaching any courses in a given semester)
- CLASSROOM **partially participates** in SCHEDULED_IN (classrooms may be unused)

## Key Assumptions & Business Rules

1. Each student must have a unique email address
2. Each course has a unique course code (e.g., "CS157A")
3. Course credits must be positive (> 0)
4. Classroom capacity must be positive (> 0)
5. Students can enroll in multiple courses per semester
6. Each course section is taught by exactly one instructor
7. Multiple course sections can use the same classroom at different times
8. Grades are recorded per student per course per semester
