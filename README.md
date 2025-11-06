# CS 157A Personal Database Application

In this semester-long project, teams of four will design and build a complete database application end-to-end. You'll model a real domain (≥5 base tables), implement it in MySQL Workbench, write non-trivial SQL (joins, subqueries, aggregates, indexes, transactions), and integrate it with a small Java console app using JDBC (no web/GUI). The work is delivered in phases with short demos, and each member's role and concrete contributions must be defined and documented before every presentation.

_This spec is a living document, minor clarifications may be added as we progress._

## Teams

- You will work in groups of 4 (randomly assigned)
- Group lists are posted on Canvas
- All membership changes require instructor approval
- Teams must define and document each member's role before every submission or presentation and should be included in the submission
- Roles may rotate across phases, but every student must own clearly identifiable work

## Tech Stack

- **Database**: MySQL (Workbench)
- **App**: Java (console), IDE, JDBC, DB config files
- **No web required**

## AI Collaboration for Project

Use AI as a teammate for brainstorming, critique, and debugging, not as an answer key. You must adapt ideas to your domain or context and prove your own understanding. This process will be part of the grade.

### What you must turn in

**ai_log.md** or **.txt**: for each AI session, include:

- Prompt used on AI (paste it)
- AI idea (3 bullets)
- We adopted/changed/rejected (3+ bullets) and contextualized + why
- What AI got wrong/generic, how you fixed/validated it

## Project Submission Phases

### Phase A: Parts 1 & 2

**Window**: Tuesday and Thursday, Sept 22–26 (both class days, mixed with usual lecture)

**Deliverables**:

- Part 1: Domain + E/R diagram + assumptions
- Part 2: Relational schema (tables, types, PK/FK, constraints) + brief normalization notes

**Phase A Deliverables**:

- PDF File of ERD Diagrams and explanations of the diagram
- PDF File of Schema mappings and explanations
- Text File of Documentation of how you incorporated AI

### Phase B: Parts 3 & 4

**Window**: Tuesday and Thursday, Nov 3–7 (both class days, mixed with usual lecture)

**Deliverables**:

- Part 3: MySQL scripts to create & populate the DB
- Part 4: Query suite (joins, subqueries, updates, indexes, and one transaction demo (commit/rollback))

### Phase C: Parts 5, 6 & 7 (Final)

**Window**: Tuesday and Thursday, Dec 1–5 (both class days - complete week given to submissions)

**Deliverables**:

- Part 5: Java console app using JDBC (PreparedStatements only, transactional workflow, input validation, etc)
- Part 6: At least one VIEW and one TRIGGER or stored routine, with a tiny test
- Part 7: Wrap-up: reproducible build/run instructions and a short reflection

---

_These are the current specifications. As we progress, I may add clarifications or small adjustments and announce on CANVAS and update the document._

## Detailed Part Requirements

### Part 1: Pick a domain + E/R design

- **Minimum schema size**: at least 5 base tables or more (not counting views)
- Choose a real domain you care about (e.g., advising, events, library, housing)
- Draw an E/R diagram with entities, relationships, cardinalities, and key choices rationale and description
- State assumptions/business rules

#### Minimum Requirements for ERD Diagrams

**Entity sets**:

- At least five or more entities (strong entities) relevant to the application domain
- At least one or more weak entities, clear distinction between strong entities and weak entities

**Attributes**:

- Each entity has at least one or more attributes (beyond the key)
- Composite keys / multi-attribute keys correctly underlined if needed in your diagram

**Relationships**:

- Relationships between entity sets shown as diamonds
- Participation (total vs partial) clearly indicated where needed

**Multiplicity constraints**:

- Each relationship must specify how many instances can participate (1–1, 1–M, M–M, notation)

**Special constructs where relevant**:

- Weak entities with identifying relationships
- ISA hierarchies (triangles) if the domain suggested specialization

**Faithfulness to domain**:

- Entities, attributes, and relationships should reflect real-world objects and connections in the project topic
- Avoid redundancy
- Keep diagrams reasonably simple (no invented intermediate entities without purpose)

### Part 2: Translate E/R to Relational schema

- Convert the E/R to relations (tables)
- Specify PKs, FKs, and constraints; justify any surrogate keys
- Brief normalization notes applicable to your database

### Part 3: Implement & seed in MySQL (Workbench)

- Create the schema in MySQL WorkBench
- Load an initial dataset that's rich enough to support interesting queries
- Submit all the schema creating statements and queries
- Evidence it runs clean

### Part 4: Queries, updates, indexes, transactions

Prepare a query suite that demonstrates:

- Multi-table joins (inner + at least one outer join)
- Subqueries (scalar, IN/EXISTS, and a FROM-subquery)
- Aggregates with GROUP BY/HAVING
- Updates/inserts/deletes
- Transactions with START TRANSACTION … COMMIT/ROLLBACK
- Add at least two indexes and explain why (and show a quick EXPLAIN before/after or timing note)

### Part 5: Java integration (console app, JDBC)

- Build a menu-driven console app using JDBC (no web/GUI)
- PreparedStatement everywhere (no string concat)
- At least one transactional workflow (e.g., multi-table insert with rollback on failure)
- Input validation and helpful error messages
- App reads DB config from a properties file

### Part 6: Views, constraints, triggers (MySQL)

- Add at least one view (e.g., a convenient reporting view)
- Add one of: a trigger or a stored routine (procedure/function) that enforces a rule or automates a task
- Include any CHECK constraints you need
- Demonstrate each feature with a tiny test

### Part 7: Wrap-up & reproducibility

Ensure a grader can build and run from scratch:

- Run `create_and_populate.sql`
- Run a subset of `queries.sql`
- Compile/run your Java app with `app.properties`
- Reflect briefly on limitations & future work

## Quality Checklist

- **Data model**: sensible PK/FK choices, integrity constraints, normalization rationale
- **SQL depth**: joins, subqueries (incl. EXISTS/IN and FROM-subquery), aggregates, DML
- **Correctness & safety**: prepared statements, transactions with rollback, error handling
- **DB features**: views + triggers/routines used meaningfully
- **Reproducibility**: one-command rebuild; properties-driven config; clear README

_No website or GUI needed: MySQL Workbench + console Java (JDBC) is the intended stack._





 CREATE DATABASE SchoolSys;
 USE SchoolSys;
 
-- Drop in FK-safe order
DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Classroom;
DROP TABLE IF EXISTS Instructor;
DROP TABLE IF EXISTS Student;

-- 1) Strong entities
CREATE TABLE Student (
  student_id   INT PRIMARY KEY AUTO_INCREMENT,
  first_name   VARCHAR(50) NOT NULL,
  last_name    VARCHAR(50) NOT NULL,
  dob          DATE,
  email        VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE Instructor (
  instructor_id INT PRIMARY KEY AUTO_INCREMENT,
  first_name    VARCHAR(50) NOT NULL,
  last_name     VARCHAR(50) NOT NULL,
  email         VARCHAR(100) NOT NULL UNIQUE,
  department    VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE Classroom (
  classroom_id INT PRIMARY KEY AUTO_INCREMENT,
  building     VARCHAR(50) NOT NULL,
  room_number  VARCHAR(10) NOT NULL,
  capacity     SMALLINT NOT NULL,
  CHECK (capacity > 0)
) ENGINE=InnoDB;

-- 2) Course (FKs to Instructor, Classroom)
CREATE TABLE Course (
  course_id     INT PRIMARY KEY AUTO_INCREMENT,
  course_name   VARCHAR(100) NOT NULL,
  course_code   VARCHAR(20)  NOT NULL UNIQUE,
  credits       TINYINT      NOT NULL,
  instructor_id INT NOT NULL,
  classroom_id  INT NOT NULL,
  CHECK (credits BETWEEN 1 AND 6),
  CONSTRAINT fk_course_instructor
    FOREIGN KEY (instructor_id) REFERENCES Instructor(instructor_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_course_classroom
    FOREIGN KEY (classroom_id)  REFERENCES Classroom(classroom_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3) Enrollment (bridge; composite PK; FKs to Student, Course)
CREATE TABLE Enrollment (
  student_id INT NOT NULL,
  course_id  INT NOT NULL,
  semester   VARCHAR(20) NOT NULL,
  grade      CHAR(2) NULL,
  PRIMARY KEY (student_id, course_id, semester),
  CONSTRAINT fk_enroll_student
    FOREIGN KEY (student_id) REFERENCES Student(student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_enroll_course
    FOREIGN KEY (course_id)  REFERENCES Course(course_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CHECK (grade IN ('A','A-','B+','B','B-','C+','C','C-','D','F') OR grade IS NULL)
) ENGINE=InnoDB;

-- Helpful indexes
CREATE INDEX idx_enroll_student_sem ON Enrollment(student_id, semester);
CREATE INDEX idx_enroll_course_sem  ON Enrollment(course_id, semester);
-- (course_code, email already UNIQUE)

-- TRIGGER: prevent enrolling beyond room capacity
DELIMITER $$
CREATE TRIGGER trg_enrollment_capacity
BEFORE INSERT ON Enrollment
FOR EACH ROW
BEGIN
  DECLARE v_classroom_id INT;
  DECLARE v_capacity SMALLINT;
  DECLARE v_current INT;

  -- classroom for this course
  SELECT classroom_id INTO v_classroom_id
  FROM Course
  WHERE course_id = NEW.course_id;

  -- room capacity
  SELECT capacity INTO v_capacity
  FROM Classroom
  WHERE classroom_id = v_classroom_id;

  -- current count for this course + semester
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


-- INSTRUCTORS
INSERT INTO Instructor (first_name, last_name, email, department) VALUES
('John',   'Smith',  'jsmith@sjsu.edu',     'Computer Science'),
('Amira',  'Patel',  'apatel@sjsu.edu',     'Mathematics'),
('Mark',   'Thomas', 'markthomas@sjsu.edu', 'Physics'),
('Grace',  'Nguyen', 'gnguyen@sjsu.edu',    'Computer Science'),
('David',  'Lopez',  'dlopez@sjsu.edu',     'Mathematics');

-- CLASSROOMS (small capacities to demo trigger)
INSERT INTO Classroom (building, room_number, capacity) VALUES
('ENG', '105',  2),
('SCI', '210',  3),
('BUS', '301',  4),
('ENG', '220',  2);

-- COURSES (resolve FKs by natural keys)
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

-- STUDENTS
INSERT INTO Student (first_name, last_name, dob, email) VALUES
('Sarah',  'Lee',   '2004-08-15', 'sarah.lee@sjsu.edu'),
('Miguel', 'Chen',  '2003-11-03', 'miguel.chen@sjsu.edu'),
('Priya',  'Nair',  '2004-02-22', 'priya.nair@sjsu.edu'),
('Ethan',  'Park',  '2003-05-09', 'ethan.park@sjsu.edu'),
('Aisha',  'Khan',  '2005-01-28', 'aisha.khan@sjsu.edu'),
('Luis',   'Garcia','2004-12-11', 'luis.garcia@sjsu.edu');

-- ============================
-- ENROLLMENTS (multiple terms)
-- ============================

-- Roster helpers (subqueries for course_id)
--   CS157A  → (SELECT course_id FROM Course WHERE course_code = 'CS157A')
--   MATH161 → (SELECT course_id FROM Course WHERE course_code = 'MATH161')
--   CS149   → (SELECT course_id FROM Course WHERE course_code = 'CS149')
--   MATH129A→ (SELECT course_id FROM Course WHERE course_code = 'MATH129A')

-- Fall 2025 (ENG 105 capacity = 2 → use to demo capacity trigger)
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

-- This third insert into CS157A (same semester) EXCEEDS ENG 105 capacity (2) → will trigger error.
-- Uncomment to test:
-- INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
-- ((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
--  (SELECT course_id  FROM Course  WHERE course_code = 'CS157A'),
--  'Fall 2025', NULL);

-- Spring 2026 (new term; some grades posted)
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

-- Fall 2026 (mix of courses; some NULL grades)
INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES
((SELECT student_id FROM Student WHERE email = 'aisha.khan@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'CS149'),
 'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email = 'luis.garcia@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
 'Fall 2026', NULL),
((SELECT student_id FROM Student WHERE email = 'ethan.park@sjsu.edu'),
 (SELECT course_id  FROM Course  WHERE course_code = 'MATH161'),
 'Fall 2026', NULL);


-- QUERIES


-- Course rosters by term
SELECT s.first_name, s.last_name, e.semester, c.course_code
FROM Enrollment e
JOIN Student s ON s.student_id = e.student_id
JOIN Course  c ON c.course_id  = e.course_id
ORDER BY e.semester, c.course_code, s.last_name;

-- Classroom load for a term
-- SELECT cl.building, cl.room_number, c.course_code, e.semester, COUNT(*) AS enrolled
-- FROM Enrollment e
-- JOIN Course c   ON c.course_id = e.course_id
-- JOIN Classroom cl ON cl.classroom_id = c.classroom_id
-- WHERE e.semester = 'Fall 2025'
-- GROUP BY cl.building, cl.room_number, c.course_code, e.semester;

-- Student transcript-like view
-- SELECT s.first_name, s.last_name, e.semester, c.course_code, c.course_name, e.grade
-- FROM Enrollment e
-- JOIN Student s ON s.student_id = e.student_id
-- JOIN Course  c ON c.course_id  = e.course_id
-- WHERE s.email = 'sarah.lee@sjsu.edu'
-- ORDER BY e.semester;








