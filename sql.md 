 CREATE DATABASE SchoolSys;
 USE SchoolSys;
 
DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Classroom;
DROP TABLE IF EXISTS Instructor;
DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
  student_id   INT PRIMARY KEY AUTO_INCREMENT,
  first_name   VARCHAR(50) NOT NULL,
  last_name    VARCHAR(50) NOT NULL,
  dob          DATE,
  email        VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;


CREATE TABLE Instructor (
  instructor_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name  VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  department VARCHAR(100)
) ENGINE=InnoDB;


CREATE TABLE Classroom (
  classroom_id INT PRIMARY KEY AUTO_INCREMENT,
  building     VARCHAR(50) NOT NULL,
  room_number  VARCHAR(10) NOT NULL,
  capacity     SMALLINT NOT NULL,
  CHECK (capacity > 0)
) ENGINE=InnoDB;


CREATE TABLE Course (
  course_id  INT PRIMARY KEY AUTO_INCREMENT,
  course_name  VARCHAR(100) NOT NULL,
  course_code VARCHAR(20)  NOT NULL UNIQUE,
  credits TINYINT      NOT NULL,
  instructor_id INT NOT NULL,
  classroom_id INT NOT NULL,
  CHECK (credits BETWEEN 1 AND 6),
  CONSTRAINT fk_course_instructor
    FOREIGN KEY (instructor_id) REFERENCES Instructor(instructor_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_course_classroom
    FOREIGN KEY (classroom_id) REFERENCES Classroom(classroom_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;


CREATE TABLE Enrollment (
  student_id INT NOT NULL,
  course_id  INT NOT NULL,
  semester VARCHAR(20) NOT NULL,
  grade CHAR(2) NULL,
  PRIMARY KEY (student_id, course_id, semester),
  CONSTRAINT fk_enroll_student
    FOREIGN KEY (student_id) REFERENCES Student(student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_enroll_course
    FOREIGN KEY (course_id)  REFERENCES Course(course_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (grade IN ('A','A-','B+','B','B-','C+','C','C-','D','F') OR grade IS NULL)
) ENGINE=InnoDB;


CREATE INDEX idx_enroll_student_sem ON Enrollment(student_id, semester);
CREATE INDEX idx_enroll_course_sem  ON Enrollment(course_id, semester);



DELIMITER $$
CREATE TRIGGER trg_enrollment_capacity
BEFORE INSERT ON Enrollment
FOR EACH ROW
BEGIN
  DECLARE v_classroom_id INT;
  DECLARE v_capacity SMALLINT;
  DECLARE v_current INT;

  SELECT classroom_id INTO v_classroom_id
  FROM Course
  WHERE course_id = NEW.course_id;

  
  SELECT capacity INTO v_capacity
  FROM Classroom
  WHERE classroom_id = v_classroom_id;

  SELECT COUNT(*) INTO v_current
  FROM Enrollment
  WHERE course_id = NEW.course_id
    AND semester  = NEW.semester;

  IF v_current >= v_capacity THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Enrollment blocked: classroom capacity exceeded';
  END IF;
END$$
DELIMITER ;


INSERT INTO Instructor (first_name, last_name, email, department) VALUES
('John',   'Smith',  'jsmith@sjsu.edu',     'Computer Science'),
('Amira',  'Patel',  'apatel@sjsu.edu',     'Mathematics'),
('Mark',   'Thomas', 'markthomas@sjsu.edu', 'Physics'),
('Grace',  'Nguyen', 'gnguyen@sjsu.edu',    'Computer Science'),
('David',  'Lopez',  'dlopez@sjsu.edu',     'Mathematics');

INSERT INTO Classroom (building, room_number, capacity) VALUES
('ENG', '105',  2),
('SCI', '210',  3),
('BUS', '301',  4),
('ENG', '220',  2);


INSERT INTO Course (course_name, course_code, credits, instructor_id, classroom_id) VALUES
('Database Systems',     'CS157A', 3,
  (SELECT instructor_id FROM Instructor WHERE email = 'jsmith@sjsu.edu'),
  (SELECT classroom_id  FROM Classroom  WHERE building = 'ENG' AND room_number = '105')),
('Discrete Mathematics', 'MATH161', 3,
  (SELECT instructor_id FROM Instructor WHERE email = 'apatel@sjsu.edu'),
  (SELECT classroom_id  FROM Classroom  WHERE building = 'SCI' AND room_number = '210')),
('Operating Systems',    'CS149',  4,
  (SELECT instructor_id FROM Instructor WHERE email = 'gnguyen@sjsu.edu'),
  (SELECT classroom_id  FROM Classroom  WHERE building = 'BUS' AND room_number = '301')),
('Linear Algebra',       'MATH129A', 3,
  (SELECT instructor_id FROM Instructor WHERE email = 'dlopez@sjsu.edu'),
  (SELECT classroom_id  FROM Classroom  WHERE building = 'ENG' AND room_number = '220'));
  
INSERT INTO Student (first_name, last_name, dob, email) VALUES
('Sarah',  'Lee',   '2004-08-15', 'sarah.lee@sjsu.edu'),
('Miguel', 'Chen',  '2003-11-03', 'miguel.chen@sjsu.edu'),
('Priya',  'Nair',  '2004-02-22', 'priya.nair@sjsu.edu'),
('Ethan',  'Park',  '2003-05-09', 'ethan.park@sjsu.edu'),
('Aisha',  'Khan',  '2005-01-28', 'aisha.khan@sjsu.edu'),
('Luis',   'Garcia','2004-12-11', 'luis.garcia@sjsu.edu');


-- Fall 2025 
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'sarah.lee@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
 'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email = 'miguel.chen@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
 'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email = 'priya.nair@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
 'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email = 'ethan.park@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
 'Fall 2025', NULL);



-- Spring 2026 
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'sarah.lee@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS149'),
 'Spring 2026', 'A'),
((SELECT student_id FROM Student WHERE email = 'miguel.chen@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS149'),
 'Spring 2026', 'B+'),
((SELECT student_id FROM Student WHERE email = 'priya.nair@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH129A'),
 'Spring 2026', 'A-'),
((SELECT student_id FROM Student WHERE email = 'luis.garcia@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH129A'),
 'Spring 2026', 'B');
 
 -- This third insert into CS157A (same semester) EXCEEDS ENG 105 capacity (2) and causes will trigger error.
-- Uncomment to test:
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--  (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
--  'Fall 2025', NULL);
 

-- Fall 2026 (mix of courses; some NULL grades)
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
 (SELECT course_id FROM Course  WHERE course_code = 'CS149'),
 'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email = 'luis.garcia@sjsu.edu'),
 (SELECT course_id FROM Course  WHERE course_code = 'MATH161'), 'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email = 'ethan.park@sjsu.edu'),
 (SELECT course_id FROM Course  WHERE course_code = 'MATH161'),
 'Fall 2026', NULL);


-- Course rosters by term
SELECT s.first_name, s.last_name, e.semester, c.course_code
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course  c ON c.course_id  = e.course_id
ORDER BY e.semester, c.course_code, s.last_name;

-- Classroom load for a term
SELECT cl.building, cl.room_number, c.course_code, e.semester, COUNT(*) AS enrolled
FROM Enrollment e
JOIN Course c   ON c.course_id = e.course_id
JOIN Classroom cl ON cl.classroom_id = c.classroom_id    -- classroom and course table
	WHERE e.semester = 'Fall 2025'
	GROUP BY cl.building, cl.room_number, c.course_code, e.semester;

-- Student transcript-like view
SELECT s.first_name, s.last_name, e.semester, c.course_code, c.course_name, e.grade
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course  c ON c.course_id  = e.course_id
	WHERE s.email = 'sarah.lee@sjsu.edu'
	ORDER BY e.semester;


-- Instructors with no enrollments in Fall 2025
SELECT i.instructor_id, i.first_name, i.last_name, i.department
FROM Instructor i
WHERE NOT EXISTS (
    SELECT 1
    FROM Course c
    JOIN Enrollment e ON e.course_id = c.course_id
    WHERE c.instructor_id = i.instructor_id
      AND e.semester = 'Fall 2025'
);


SELECT e.semester, c.course_code, e.grade
FROM Enrollment e USE INDEX (idx_enroll_student_sem)
JOIN Course c ON c.course_id = e.course_id
WHERE e.student_id = (SELECT student_id FROM Student WHERE email = 'sarah.lee@sjsu.edu')
  AND e.semester = 'Fall 2025';


UPDATE Enrollment
SET grade = 'A-'
WHERE student_id = (SELECT student_id FROM Student WHERE email = 'miguel.chen@sjsu.edu')
  AND course_id  = (SELECT course_id  FROM Course  WHERE course_code = 'CS149')
  AND semester   = 'Spring 2026';

SELECT e.student_id, e.course_id, e.semester, e.grade, c.course_code
FROM Enrollment e
JOIN Course c ON e.course_id = c.course_id
WHERE c.course_code = 'CS149';







