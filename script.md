Part 1: Creating the Schema and Base Tables
CREATE DATABASE SchoolSys;
USE SchoolSys;

DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Classroom;
DROP TABLE IF EXISTS Instructor;
DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
student_id INT PRIMARY KEY AUTO_INCREMENT,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
dob DATE,
email VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE Instructor (
instructor_id INT PRIMARY KEY AUTO_INCREMENT,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
department VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE Classroom (
classroom_id INT PRIMARY KEY AUTO_INCREMENT,
building VARCHAR(50) NOT NULL,
room_number VARCHAR(10) NOT NULL,
capacity SMALLINT NOT NULL,
CHECK (capacity > 0)
) ENGINE=InnoDB;

🗣️ Explanation (1 minute)

We start by creating a brand-new database called SchoolSys and clear any old tables to avoid conflicts.
Then we define three main entities — students, instructors, and classrooms.

Each has an auto-increment primary key so we don’t have to manually assign IDs.
We also make sure student and instructor emails are unique, and classroom capacity has to be above zero.
By setting the engine to InnoDB, we’ll later be able to use foreign keys for relationships.
So at this point, we’ve set up the foundation — who’s in the system and what rooms they can use.

Part 2: Adding Courses, Enrollments, and the Capacity Trigger
CREATE TABLE Course (
course_id INT PRIMARY KEY AUTO_INCREMENT,
course_name VARCHAR(100) NOT NULL,
course_code VARCHAR(20) NOT NULL UNIQUE,
credits TINYINT NOT NULL,
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
course_id INT NOT NULL,
semester VARCHAR(20) NOT NULL,
grade CHAR(2) NULL,
PRIMARY KEY (student_id, course_id, semester),
CONSTRAINT fk_enroll_student
FOREIGN KEY (student_id) REFERENCES Student(student_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_enroll_course
FOREIGN KEY (course_id) REFERENCES Course(course_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CHECK (grade IN ('A','A-','B+','B','B-','C+','C','C-','D','F') OR grade IS NULL)
) ENGINE=InnoDB;

CREATE INDEX idx_enroll_student_sem ON Enrollment(student_id, semester);
CREATE INDEX idx_enroll_course_sem ON Enrollment(course_id, semester);

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

SELECT COUNT(\*) INTO v_current
FROM Enrollment
WHERE course_id = NEW.course_id
AND semester = NEW.semester;

IF v_current >= v_capacity THEN
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Enrollment blocked: classroom capacity exceeded';
END IF;
END$$
DELIMITER ;

🗣️ Explanation (1 minute)

Now we create Course and Enrollment, and wire them together with foreign keys.
A course is tied to one instructor and one classroom.
Credits have to stay between 1 and 6.

The Enrollment table acts as a bridge between students and courses, allowing us to track who’s enrolled in what semester, along with their grades.
We also add a couple of indexes to make lookups faster.

Finally, we build a trigger that prevents over-enrollment.
Whenever a new student tries to join a class, the trigger checks how many students are already enrolled.
If the number hits the classroom’s capacity, the trigger stops the insert with a clear error message.

Part 3: Loading Sample Data
INSERT INTO Instructor (first_name, last_name, email, department) VALUES
('John','Smith','jsmith@sjsu.edu','Computer Science'),
('Amira','Patel','apatel@sjsu.edu','Mathematics'),
('Mark','Thomas','markthomas@sjsu.edu','Physics'),
('Grace','Nguyen','gnguyen@sjsu.edu','Computer Science'),
('David','Lopez','dlopez@sjsu.edu','Mathematics');

INSERT INTO Classroom (building, room_number, capacity) VALUES
('ENG','105',2),
('SCI','210',3),
('BUS','301',4),
('ENG','220',2);

INSERT INTO Course (course_name, course_code, credits, instructor_id, classroom_id) VALUES
('Database Systems','CS157A',3,
(SELECT instructor_id FROM Instructor WHERE email='jsmith@sjsu.edu'),
(SELECT classroom_id FROM Classroom WHERE building='ENG' AND room_number='105')),
('Discrete Mathematics','MATH161',3,
(SELECT instructor_id FROM Instructor WHERE email='apatel@sjsu.edu'),
(SELECT classroom_id FROM Classroom WHERE building='SCI' AND room_number='210')),
('Operating Systems','CS149',4,
(SELECT instructor_id FROM Instructor WHERE email='gnguyen@sjsu.edu'),
(SELECT classroom_id FROM Classroom WHERE building='BUS' AND room_number='301')),
('Linear Algebra','MATH129A',3,
(SELECT instructor_id FROM Instructor WHERE email='dlopez@sjsu.edu'),
(SELECT classroom_id FROM Classroom WHERE building='ENG' AND room_number='220'));

INSERT INTO Student (first_name,last_name,dob,email) VALUES
('Sarah','Lee','2004-08-15','sarah.lee@sjsu.edu'),
('Miguel','Chen','2003-11-03','miguel.chen@sjsu.edu'),
('Priya','Nair','2004-02-22','priya.nair@sjsu.edu'),
('Ethan','Park','2003-05-09','ethan.park@sjsu.edu'),
('Aisha','Khan','2005-01-28','aisha.khan@sjsu.edu'),
('Luis','Garcia','2004-12-11','luis.garcia@sjsu.edu');

🗣️ Explanation (1 minute)

Here we’re populating the system with real sample data.
We start with instructors and classrooms, since courses depend on both.
When we insert courses, we use subqueries to automatically grab the correct instructor and classroom IDs — that way we don’t need to hardcode numbers.

Finally, we load a few students with names, birthdays, and emails.
At this stage, the database has everything ready: teachers, rooms, classes, and students — but nobody’s enrolled yet.

Part 4: Enrollments, Capacity Test, and Reports
-- Fall 2025
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email='sarah.lee@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='CS157A'),
'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email='miguel.chen@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='CS157A'),
'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email='priya.nair@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH161'),
'Fall 2025', NULL),
((SELECT student_id FROM Student WHERE email='ethan.park@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH161'),
'Fall 2025', NULL);

-- Spring 2026
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email='sarah.lee@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='CS149'),
'Spring 2026', 'A'),
((SELECT student_id FROM Student WHERE email='miguel.chen@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='CS149'),
'Spring 2026', 'B+'),
((SELECT student_id FROM Student WHERE email='priya.nair@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH129A'),
'Spring 2026', 'A-'),
((SELECT student_id FROM Student WHERE email='luis.garcia@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH129A'),
'Spring 2026', 'B');

-- Capacity test (commented)
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email='aisha.khan@sjsu.edu'),
-- (SELECT course_id FROM Course WHERE course_code='CS157A'),
-- 'Fall 2025', NULL);

-- Fall 2026
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email='aisha.khan@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='CS149'),
'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email='luis.garcia@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH161'),
'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email='ethan.park@sjsu.edu'),
(SELECT course_id FROM Course WHERE course_code='MATH161'),
'Fall 2026', NULL);

-- Reports
SELECT s.first_name, s.last_name, e.semester, c.course_code
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course c ON c.course_id = e.course_id
ORDER BY e.semester, c.course_code, s.last_name;

SELECT cl.building, cl.room_number, c.course_code, e.semester, COUNT(\*) AS enrolled
FROM Enrollment e
JOIN Course c ON c.course_id = e.course_id
JOIN Classroom cl ON cl.classroom_id = c.classroom_id
WHERE e.semester = 'Fall 2025'
GROUP BY cl.building, cl.room_number, c.course_code, e.semester;

SELECT s.first_name, s.last_name, e.semester, c.course_code, c.course_name, e.grade
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course c ON c.course_id = e.course_id
WHERE s.email = 'sarah.lee@sjsu.edu'
ORDER BY e.semester;

SELECT i.instructor_id, i.first_name, i.last_name, i.department
FROM Instructor i
WHERE NOT EXISTS (
SELECT 1
FROM Course c
JOIN Enrollment e ON e.course_id = c.course_id
WHERE c.instructor_id = i.instructor_id
AND e.semester = 'Fall 2025'
);

🗣️ Explanation (1 minute)

Now we’re enrolling students across three semesters.
For Fall 2025, Sarah and Miguel join CS157A, and Priya and Ethan take Math161.
In Spring 2026, we add more courses and even assign grades.
If you uncomment the “capacity test” insert, it’ll trigger an error — that shows our capacity rule works.

Then we add a few Fall 2026 enrollments just to mix it up.

After that, we run a few reports:

The first lists all rosters by term.

The second shows how many students are in each classroom during Fall 2025.

The third is like a mini transcript for Sarah Lee.

And finally, we check which instructors had no classes in Fall 2025.

By this point, we’ve built a small but realistic school database with rules, constraints, and reports that actually make sense.
