-- CS157A Final Project - School Management System
-- Database setup script
-- Run this to create everything from scratch
CREATE DATABASE IF NOT EXISTS SchoolSys;
USE SchoolSys;
-- drop tables first (need to do it in right order because of foreign keys)
DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Classroom;
DROP TABLE IF EXISTS Instructor;
DROP TABLE IF EXISTS Student;
-- drop views and procedures if they exist
DROP VIEW IF EXISTS student_transcript_view;
DROP PROCEDURE IF EXISTS sp_enroll_student_in_course;
-- create tables
CREATE TABLE Student (
  student_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  dob DATE,
  email VARCHAR(100) NOT NULL UNIQUE
) ENGINE = InnoDB;
CREATE TABLE Instructor (
  instructor_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  department VARCHAR(100)
) ENGINE = InnoDB;
CREATE TABLE Classroom (
  classroom_id INT PRIMARY KEY AUTO_INCREMENT,
  building VARCHAR(50) NOT NULL,
  room_number VARCHAR(10) NOT NULL,
  capacity SMALLINT NOT NULL,
  CHECK (capacity > 0)
) ENGINE = InnoDB;
-- course table (has foreign keys to instructor and classroom)
CREATE TABLE Course (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  course_name VARCHAR(100) NOT NULL,
  course_code VARCHAR(20) NOT NULL UNIQUE,
  credits TINYINT NOT NULL,
  instructor_id INT NOT NULL,
  classroom_id INT NOT NULL,
  CHECK (
    credits BETWEEN 1 AND 6
  ),
  CONSTRAINT fk_course_instructor FOREIGN KEY (instructor_id) REFERENCES Instructor(instructor_id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_course_classroom FOREIGN KEY (classroom_id) REFERENCES Classroom(classroom_id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;
-- enrollment table (many-to-many between student and course, composite primary key)
CREATE TABLE Enrollment (
  student_id INT NOT NULL,
  course_id INT NOT NULL,
  semester VARCHAR(20) NOT NULL,
  grade CHAR(2) NULL,
  PRIMARY KEY (student_id, course_id, semester),
  CONSTRAINT fk_enroll_student FOREIGN KEY (student_id) REFERENCES Student(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_enroll_course FOREIGN KEY (course_id) REFERENCES Course(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (
    grade IN (
      'A',
      'A-',
      'B+',
      'B',
      'B-',
      'C+',
      'C',
      'C-',
      'D',
      'F'
    )
    OR grade IS NULL
  )
) ENGINE = InnoDB;
-- constraint to make sure semester format is correct
ALTER TABLE Enrollment
ADD CONSTRAINT chk_semester_format CHECK (
    semester REGEXP '^(Fall|Spring|Summer|Winter) [0-9]{4}$'
  );
-- indexes to speed up common queries
CREATE INDEX idx_enroll_student_sem ON Enrollment(student_id, semester);
CREATE INDEX idx_enroll_course_sem ON Enrollment(course_id, semester);
-- trigger to prevent enrolling more students than classroom capacity
DELIMITER $$ CREATE TRIGGER trg_enrollment_capacity BEFORE
INSERT ON Enrollment FOR EACH ROW BEGIN
DECLARE v_classroom_id INT;
DECLARE v_capacity SMALLINT;
DECLARE v_current INT;
-- find which classroom this course uses
SELECT classroom_id INTO v_classroom_id
FROM Course
WHERE course_id = NEW.course_id;
-- get the capacity of that classroom
SELECT capacity INTO v_capacity
FROM Classroom
WHERE classroom_id = v_classroom_id;
-- count how many students already enrolled for this course and semester
SELECT COUNT(*) INTO v_current
FROM Enrollment
WHERE course_id = NEW.course_id
  AND semester = NEW.semester;
-- if at capacity, block the enrollment
IF v_current >= v_capacity THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Enrollment blocked: classroom capacity exceeded';
END IF;
END $$ DELIMITER;
-- view to make it easier to get student transcripts
-- joins student, enrollment, course, and instructor tables together
CREATE VIEW student_transcript_view AS
SELECT s.student_id,
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
ORDER BY s.student_id,
  e.semester,
  c.course_code;
-- stored procedure to enroll a student in a course
-- checks if student/course exist, checks for duplicates, checks capacity, then enrolls
DELIMITER $$ CREATE PROCEDURE sp_enroll_student_in_course(
  IN p_student_id INT,
  IN p_course_id INT,
  IN p_semester VARCHAR(20),
  IN p_grade CHAR(2),
  OUT p_status VARCHAR(200),
  OUT p_success BOOLEAN
) BEGIN
DECLARE v_student_exists INT DEFAULT 0;
DECLARE v_course_exists INT DEFAULT 0;
DECLARE v_duplicate_exists INT DEFAULT 0;
DECLARE v_classroom_id INT;
DECLARE v_capacity SMALLINT;
DECLARE v_current_enrolled INT;
-- set default values
SET p_success = FALSE;
SET p_status = '';
-- check if student exists
SELECT COUNT(*) INTO v_student_exists
FROM Student
WHERE student_id = p_student_id;
IF v_student_exists = 0 THEN
SET p_status = 'Error: Student not found';
SET p_success = FALSE;
ELSE -- check if course exists
SELECT COUNT(*) INTO v_course_exists
FROM Course
WHERE course_id = p_course_id;
IF v_course_exists = 0 THEN
SET p_status = 'Error: Course not found';
SET p_success = FALSE;
ELSE -- check if already enrolled
SELECT COUNT(*) INTO v_duplicate_exists
FROM Enrollment
WHERE student_id = p_student_id
  AND course_id = p_course_id
  AND semester = p_semester;
IF v_duplicate_exists > 0 THEN
SET p_status = 'Error: Student is already enrolled in this course for this semester';
SET p_success = FALSE;
ELSE -- check classroom capacity
SELECT classroom_id INTO v_classroom_id
FROM Course
WHERE course_id = p_course_id;
SELECT capacity INTO v_capacity
FROM Classroom
WHERE classroom_id = v_classroom_id;
SELECT COUNT(*) INTO v_current_enrolled
FROM Enrollment
WHERE course_id = p_course_id
  AND semester = p_semester;
IF v_current_enrolled >= v_capacity THEN
SET p_status = CONCAT(
    'Error: Classroom capacity exceeded (',
    v_capacity,
    ' students)'
  );
SET p_success = FALSE;
ELSE -- check grade format if grade was provided
IF p_grade IS NOT NULL
AND p_grade NOT IN (
  'A',
  'A-',
  'B+',
  'B',
  'B-',
  'C+',
  'C',
  'C-',
  'D',
  'F'
) THEN
SET p_status = 'Error: Invalid grade format';
SET p_success = FALSE;
ELSE -- everything looks good, insert the enrollment
INSERT INTO Enrollment (student_id, course_id, semester, grade)
VALUES (p_student_id, p_course_id, p_semester, p_grade);
SET p_status = 'Student enrolled successfully';
SET p_success = TRUE;
END IF;
END IF;
END IF;
END IF;
END IF;
END $$ DELIMITER;
-- insert sample data
-- instructors
INSERT INTO Instructor (first_name, last_name, email, department)
VALUES (
    'John',
    'Smith',
    'jsmith@sjsu.edu',
    'Computer Science'
  ),
  (
    'Amira',
    'Patel',
    'apatel@sjsu.edu',
    'Mathematics'
  ),
  (
    'Mark',
    'Thomas',
    'markthomas@sjsu.edu',
    'Physics'
  ),
  (
    'Grace',
    'Nguyen',
    'gnguyen@sjsu.edu',
    'Computer Science'
  ),
  (
    'David',
    'Lopez',
    'dlopez@sjsu.edu',
    'Mathematics'
  );
-- classrooms (small capacities so we can test the trigger)
INSERT INTO Classroom (building, room_number, capacity)
VALUES ('ENG', '105', 2),
  ('SCI', '210', 3),
  ('BUS', '301', 4),
  ('ENG', '220', 2);
-- courses (using subqueries to get instructor and classroom IDs)
INSERT INTO Course (
    course_name,
    course_code,
    credits,
    instructor_id,
    classroom_id
  )
VALUES (
    'Database Systems',
    'CS157A',
    3,
    (
      SELECT instructor_id
      FROM Instructor
      WHERE email = 'jsmith@sjsu.edu'
    ),
    (
      SELECT classroom_id
      FROM Classroom
      WHERE building = 'ENG'
        AND room_number = '105'
    )
  ),
  (
    'Discrete Mathematics',
    'MATH161',
    3,
    (
      SELECT instructor_id
      FROM Instructor
      WHERE email = 'apatel@sjsu.edu'
    ),
    (
      SELECT classroom_id
      FROM Classroom
      WHERE building = 'SCI'
        AND room_number = '210'
    )
  ),
  (
    'Operating Systems',
    'CS149',
    4,
    (
      SELECT instructor_id
      FROM Instructor
      WHERE email = 'gnguyen@sjsu.edu'
    ),
    (
      SELECT classroom_id
      FROM Classroom
      WHERE building = 'BUS'
        AND room_number = '301'
    )
  ),
  (
    'Linear Algebra',
    'MATH129A',
    3,
    (
      SELECT instructor_id
      FROM Instructor
      WHERE email = 'dlopez@sjsu.edu'
    ),
    (
      SELECT classroom_id
      FROM Classroom
      WHERE building = 'ENG'
        AND room_number = '220'
    )
  );
-- students
INSERT INTO Student (first_name, last_name, dob, email)
VALUES (
    'Sarah',
    'Lee',
    '2004-08-15',
    'sarah.lee@sjsu.edu'
  ),
  (
    'Miguel',
    'Chen',
    '2003-11-03',
    'miguel.chen@sjsu.edu'
  ),
  (
    'Priya',
    'Nair',
    '2004-02-22',
    'priya.nair@sjsu.edu'
  ),
  (
    'Ethan',
    'Park',
    '2003-05-09',
    'ethan.park@sjsu.edu'
  ),
  (
    'Aisha',
    'Khan',
    '2005-01-28',
    'aisha.khan@sjsu.edu'
  ),
  (
    'Luis',
    'Garcia',
    '2004-12-11',
    'luis.garcia@sjsu.edu'
  );
-- enrollments for different semesters
-- fall 2025 - CS157A has capacity 2, so we can test the trigger
INSERT INTO Enrollment (student_id, course_id, semester, grade)
VALUES (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'sarah.lee@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'CS157A'
    ),
    'Fall 2025',
    NULL
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'miguel.chen@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'CS157A'
    ),
    'Fall 2025',
    NULL
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'priya.nair@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH161'
    ),
    'Fall 2025',
    NULL
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'ethan.park@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH161'
    ),
    'Fall 2025',
    NULL
  );
-- spring 2026 - some courses have grades
INSERT INTO Enrollment (student_id, course_id, semester, grade)
VALUES (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'sarah.lee@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'CS149'
    ),
    'Spring 2026',
    'A'
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'miguel.chen@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'CS149'
    ),
    'Spring 2026',
    'B+'
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'priya.nair@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH129A'
    ),
    'Spring 2026',
    'A-'
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'luis.garcia@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH129A'
    ),
    'Spring 2026',
    'B'
  );
-- fall 2026 - mix of courses, some grades are NULL
INSERT INTO Enrollment (student_id, course_id, semester, grade)
VALUES (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'aisha.khan@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'CS149'
    ),
    'Fall 2026',
    NULL
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'luis.garcia@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH161'
    ),
    'Fall 2026',
    NULL
  ),
  (
    (
      SELECT student_id
      FROM Student
      WHERE email = 'ethan.park@sjsu.edu'
    ),
    (
      SELECT course_id
      FROM Course
      WHERE course_code = 'MATH161'
    ),
    'Fall 2026',
    NULL
  );
-- test queries (commented out - uncomment to test)
-- test the view
-- SELECT * FROM student_transcript_view WHERE email = 'sarah.lee@sjsu.edu';
-- test the stored procedure
-- SET @status = '';
-- SET @success = FALSE;
-- CALL sp_enroll_student_in_course(
--     (SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--     (SELECT course_id FROM Course WHERE course_code = 'MATH161'),
--     'Spring 2027',
--     NULL,
--     @status,
--     @success
-- );
-- SELECT @status AS result_status, @success AS success_flag;
-- test trigger (should fail because CS157A Fall 2025 already has 2 students, capacity is 2)
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--  (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
--  'Fall 2025', NULL);
-- test constraint (invalid semester format - should fail)
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--  (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
--  'fall2025', NULL);
-- test constraint (invalid grade - should fail)
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--  (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
--  'Spring 2027', 'X');
-- verification queries (optional - uncomment to check everything)
-- show all tables
-- SHOW TABLES;
-- show view definition
-- SHOW CREATE VIEW student_transcript_view;
-- show procedure definition
-- SHOW CREATE PROCEDURE sp_enroll_student_in_course;
-- count records in each table
-- SELECT 'Student' AS table_name, COUNT(*) AS record_count FROM Student
-- UNION ALL
-- SELECT 'Instructor', COUNT(*) FROM Instructor
-- UNION ALL
-- SELECT 'Classroom', COUNT(*) FROM Classroom
-- UNION ALL
-- SELECT 'Course', COUNT(*) FROM Course
-- UNION ALL
-- SELECT 'Enrollment', COUNT(*) FROM Enrollment;