# Database Schema

## Student

| Column     | Data Type    | Constraints      |
| ---------- | ------------ | ---------------- |
| student_id | INT          | PRIMARY KEY      |
| first_name | VARCHAR(50)  | NOT NULL         |
| last_name  | VARCHAR(50)  | NOT NULL         |
| dob        | DATE         |                  |
| email      | VARCHAR(100) | UNIQUE, NOT NULL |

---

## Course

| Column      | Data Type    | Constraints        |
| ----------- | ------------ | ------------------ |
| course_id   | INT          | PRIMARY KEY        |
| course_name | VARCHAR(100) | NOT NULL           |
| course_code | VARCHAR(20)  | UNIQUE, NOT NULL   |
| credits     | INT          | CHECK(credits > 0) |

---

## Instructor

| Column        | Data Type    | Constraints      |
| ------------- | ------------ | ---------------- |
| instructor_id | INT          | PRIMARY KEY      |
| first_name    | VARCHAR(50)  | NOT NULL         |
| last_name     | VARCHAR(50)  | NOT NULL         |
| email         | VARCHAR(100) | UNIQUE, NOT NULL |
| department    | VARCHAR(100) |                  |

---

## Classroom

| Column       | Data Type   | Constraints         |
| ------------ | ----------- | ------------------- |
| classroom_id | INT         | PRIMARY KEY         |
| building     | VARCHAR(50) | NOT NULL            |
| room_number  | VARCHAR(10) | NOT NULL            |
| capacity     | INT         | CHECK(capacity > 0) |
