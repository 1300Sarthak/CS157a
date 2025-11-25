import java.io.*;
import java.sql.*;
import java.util.*;

// CS157A Final Project - School Management System
public class Main {
    private static final String PROPERTIES_FILE = "app.properties";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        
        try {
            // load database config
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            
            if (url == null || username == null || password == null) {
                System.err.println("Error: Missing database config in app.properties");
                return;
            }
            
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!\n");
            
            // main menu loop
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = getMenuChoice(scanner, 1, 9);
                
                switch (choice) {
                    case 1:
                        handleViewOperations(conn, scanner);
                        break;
                    case 2:
                        handleInsertOperations(conn, scanner);
                        break;
                    case 3:
                        handleUpdateOperations(conn, scanner);
                        break;
                    case 4:
                        handleDeleteOperations(conn, scanner);
                        break;
                    case 5:
                        handleTransactionalWorkflow(conn, scanner);
                        break;
                    case 9:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                
                if (running && choice != 9) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }
    
    // load properties file
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        FileInputStream fis = null;
        
        try {
            fis = new FileInputStream(PROPERTIES_FILE);
            props.load(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        
        return props;
    }
    
    private static void printMainMenu() {
        System.out.println("\nSchool Management System");
        System.out.println("1. View Operations");
        System.out.println("2. Insert Operations");
        System.out.println("3. Update Operations");
        System.out.println("4. Delete Operations");
        System.out.println("5. Transactional Workflow");
        System.out.println("9. Exit");
        System.out.print("Enter choice: ");
    }
    
    private static void handleViewOperations(Connection conn, Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nView Operations");
            System.out.println("1. View All Students");
            System.out.println("2. View Student by Email");
            System.out.println("3. View Student Enrollments");
            System.out.println("4. View All Courses");
            System.out.println("5. View Course by Code");
            System.out.println("6. View Course Roster");
            System.out.println("7. View Courses by Instructor");
            System.out.println("8. View All Enrollments");
            System.out.println("9. View Enrollments by Semester");
            System.out.println("10. View Enrollments by Course");
            System.out.println("11. View Student Transcript");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            
            int choice = getMenuChoice(scanner, 0, 11);
            
            try {
                switch (choice) {
                    case 1:
                        viewAllStudents(conn);
                        break;
                    case 2:
                        viewStudentByEmail(conn, scanner);
                        break;
                    case 3:
                        viewStudentEnrollments(conn, scanner);
                        break;
                    case 4:
                        viewAllCourses(conn);
                        break;
                    case 5:
                        viewCourseByCode(conn, scanner);
                        break;
                    case 6:
                        viewCourseRoster(conn, scanner);
                        break;
                    case 7:
                        viewCoursesByInstructor(conn, scanner);
                        break;
                    case 8:
                        viewAllEnrollments(conn);
                        break;
                    case 9:
                        viewEnrollmentsBySemester(conn, scanner);
                        break;
                    case 10:
                        viewEnrollmentsByCourse(conn, scanner);
                        break;
                    case 11:
                        viewStudentTranscript(conn, scanner);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }
    
    private static void handleInsertOperations(Connection conn, Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nInsert Operations");
            System.out.println("1. Add New Student");
            System.out.println("2. Add New Course");
            System.out.println("3. Enroll Student in Course");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            
            int choice = getMenuChoice(scanner, 0, 3);
            
            try {
                switch (choice) {
                    case 1:
                        addNewStudent(conn, scanner);
                        break;
                    case 2:
                        addNewCourse(conn, scanner);
                        break;
                    case 3:
                        enrollStudentInCourse(conn, scanner);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }
    
    private static void handleUpdateOperations(Connection conn, Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nUpdate Operations");
            System.out.println("1. Update Student Email");
            System.out.println("2. Update Course Credits");
            System.out.println("3. Update Grade");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            
            int choice = getMenuChoice(scanner, 0, 3);
            
            try {
                switch (choice) {
                    case 1:
                        updateStudentEmail(conn, scanner);
                        break;
                    case 2:
                        updateCourseCredits(conn, scanner);
                        break;
                    case 3:
                        updateGrade(conn, scanner);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }
    
    private static void handleDeleteOperations(Connection conn, Scanner scanner) {
        boolean back = false;
        
        while (!back) {
            System.out.println("\nDelete Operations");
            System.out.println("1. Delete Student");
            System.out.println("2. Delete Course");
            System.out.println("3. Drop Enrollment");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            
            int choice = getMenuChoice(scanner, 0, 3);
            
            try {
                switch (choice) {
                    case 1:
                        deleteStudent(conn, scanner);
                        break;
                    case 2:
                        deleteCourse(conn, scanner);
                        break;
                    case 3:
                        dropEnrollment(conn, scanner);
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }
    
    // transactional workflow - enroll student in multiple courses
    // demonstrates COMMIT and ROLLBACK
    private static void handleTransactionalWorkflow(Connection conn, Scanner scanner) {
        System.out.println("\nTransactional Workflow - Multi-Course Enrollment");
        System.out.println("If any enrollment fails, all will be rolled back.");
        System.out.println();
        
        enrollStudentInMultipleCourses(conn, scanner);
    }
    
    // enroll student in multiple courses with transaction
    private static void enrollStudentInMultipleCourses(Connection conn, Scanner scanner) {
        boolean originalAutoCommit = true;
        
        try {
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            System.out.println("Transaction started (auto-commit disabled)\n");
            
            String email = getNonEmptyString(scanner, "Enter student email: ");
            
            int studentId = getStudentIdByEmail(conn, email);
            if (studentId == -1) {
                System.out.println("Error: Student not found");
                conn.rollback();
                System.out.println("Rollback complete");
                return;
            }
            System.out.println("Student found: " + email + " (ID: " + studentId + ")");
            
            String semester = getValidSemester(scanner, "Enter semester (e.g., Fall 2025): ");
            System.out.println("Semester: " + semester);
            
            System.out.print("Enter course codes (comma separated): ");
            String coursesInput = scanner.nextLine().trim();
            if (coursesInput.isEmpty()) {
                System.out.println("Error: Need at least one course");
                conn.rollback();
                return;
            }
            String[] courseCodes = coursesInput.split(",");
            for (int i = 0; i < courseCodes.length; i++) {
                courseCodes[i] = courseCodes[i].trim().toUpperCase();
            }
            
            List<String> enrolledCourses = new ArrayList<>();
            List<String> failedCourses = new ArrayList<>();
            
            System.out.println("\nProcessing enrollments...\n");
            
            for (String courseCode : courseCodes) {
                System.out.println("Processing: " + courseCode);
                
                int courseId = getCourseIdByCode(conn, courseCode);
                if (courseId == -1) {
                    System.out.println("  Error: Course not found");
                    failedCourses.add(courseCode + " (not found)");
                    continue;
                }
                
                if (enrollmentExists(conn, studentId, courseId, semester)) {
                    System.out.println("  Error: Already enrolled");
                    failedCourses.add(courseCode + " (duplicate)");
                    continue;
                }
                
                String sql = "INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, studentId);
                    pstmt.setInt(2, courseId);
                    pstmt.setString(3, semester);
                    pstmt.setNull(4, Types.CHAR);
                    
                    pstmt.executeUpdate();
                    System.out.println("  Success");
                    enrolledCourses.add(courseCode);
                } catch (SQLException e) {
                    System.out.println("  Error: " + e.getMessage());
                    failedCourses.add(courseCode + " (" + e.getMessage() + ")");
                }
                System.out.println();
            }
            
            if (!failedCourses.isEmpty()) {
                System.out.println("TRANSACTION FAILED");
                System.out.println("Failed courses:");
                for (String failed : failedCourses) {
                    System.out.println("  - " + failed);
                }
                System.out.println("\nRolling back all changes...");
                conn.rollback();
                System.out.println("Rollback complete - no enrollments created\n");
            } else {
                System.out.println("All enrollments succeeded:");
                for (String course : enrolledCourses) {
                    System.out.println("  - " + course);
                }
                System.out.println("\nCommitting transaction...");
                conn.commit();
                System.out.println("Commit complete - all enrollments saved\n");
            }
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            try {
                conn.rollback();
                System.err.println("Rollback complete");
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit: " + e.getMessage());
            }
        }
    }
    
    // input validation helpers
    
    private static int getIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Error: Value must be between " + min + " and " + max + ". Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer. Please try again.");
            }
        }
    }
    
    private static java.sql.Date getDateInput(Scanner scanner, String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (allowEmpty && input.isEmpty()) {
                return null;
            }
            if (input.isEmpty()) {
                System.out.println("Error: Date cannot be empty. Please try again.");
                continue;
            }
            try {
                return java.sql.Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD (e.g., 2025-01-15). Please try again.");
            }
        }
    }
    
    private static String getNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Error: This field cannot be empty. Please try again.");
        }
    }
    
    private static String getValidGrade(Scanner scanner, String prompt, boolean allowEmpty) {
        String[] validGrades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            if (allowEmpty && input.isEmpty()) {
                return null;
            }
            for (String g : validGrades) {
                if (input.equals(g)) {
                    return input;
                }
            }
            System.out.println("Error: Invalid grade. Valid grades: A, A-, B+, B, B-, C+, C, C-, D, F. Please try again.");
        }
    }
    
    private static String getValidSemester(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("^(Fall|Spring|Summer|Winter) \\d{4}$")) {
                return input;
            }
            System.out.println("Error: Semester must be in format 'Fall YYYY', 'Spring YYYY', 'Summer YYYY', or 'Winter YYYY'. Please try again.");
        }
    }
    
    private static void listInstructors(Connection conn) throws SQLException {
        String sql = "SELECT instructor_id, first_name, last_name, email, department FROM Instructor ORDER BY last_name, first_name";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nAvailable Instructors");
            System.out.printf("%-5s %-25s %-30s %-20s%n", "ID", "Name", "Email", "Department");
            System.out.println("--------------------------------------------------------------------------------");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-5d %-25s %-30s %-20s%n",
                    rs.getInt("instructor_id"),
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("department") != null ? rs.getString("department") : "N/A");
            }
            if (!hasData) {
                System.out.println("No instructors found.");
            }
            System.out.println();
        }
    }
    
    private static void listClassrooms(Connection conn) throws SQLException {
        String sql = "SELECT classroom_id, building, room_number, capacity FROM Classroom ORDER BY building, room_number";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\nAvailable Classrooms");
            System.out.printf("%-5s %-15s %-15s %-10s%n", "ID", "Building", "Room", "Capacity");
            System.out.println("--------------------------------------------------------------------------------");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-5d %-15s %-15s %-10d%n",
                    rs.getInt("classroom_id"),
                    rs.getString("building"),
                    rs.getString("room_number"),
                    rs.getInt("capacity"));
            }
            if (!hasData) {
                System.out.println("No classrooms found.");
            }
            System.out.println();
        }
    }
    
    private static boolean instructorExists(Connection conn, int instructorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Instructor WHERE instructor_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, instructorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private static boolean classroomExists(Connection conn, int classroomId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Classroom WHERE classroom_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, classroomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    private static int getStudentIdByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT student_id FROM Student WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("student_id");
                }
            }
        }
        return -1;
    }
    
    private static int getCourseIdByCode(Connection conn, String courseCode) throws SQLException {
        String sql = "SELECT course_id FROM Course WHERE course_code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("course_id");
                }
            }
        }
        return -1;
    }
    
    private static boolean enrollmentExists(Connection conn, int studentId, int courseId, String semester) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Enrollment WHERE student_id = ? AND course_id = ? AND semester = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setString(3, semester);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // view operations - student
    
    private static void viewAllStudents(Connection conn) throws SQLException {
        String sql = "SELECT student_id, first_name, last_name, email, dob FROM Student ORDER BY last_name, first_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\nAll Students");
            System.out.printf("%-10s %-20s %-20s %-30s %-12s%n", 
                "ID", "First Name", "Last Name", "Email", "DOB");
            System.out.println("--------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                java.sql.Date dob = rs.getDate("dob");
                
                System.out.printf("%-10d %-20s %-20s %-30s %-12s%n",
                    id, firstName, lastName, email, dob != null ? dob.toString() : "N/A");
            }
            
            if (!hasData) {
                System.out.println("No students found.");
            }
        }
    }
    
    private static void viewStudentByEmail(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }
        
        String sql = "SELECT student_id, first_name, last_name, email, dob FROM Student WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nStudent Details");
                    System.out.println("ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("Email: " + rs.getString("email"));
                    java.sql.Date dob = rs.getDate("dob");
                    System.out.println("Date of Birth: " + (dob != null ? dob.toString() : "N/A"));
                } else {
                    System.out.println("Student not found with email: " + email);
                }
            }
        }
    }
    
    private static void viewStudentEnrollments(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        
        String sql = "SELECT e.semester, c.course_code, c.course_name, e.grade " +
                     "FROM Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "WHERE s.email = ? " +
                     "ORDER BY e.semester, c.course_code";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nEnrollments for " + email);
                System.out.printf("%-15s %-15s %-30s %-10s%n", 
                    "Semester", "Course Code", "Course Name", "Grade");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-15s %-15s %-30s %-10s%n",
                        rs.getString("semester"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("grade") != null ? rs.getString("grade") : "N/A");
                }
                
                if (!hasData) {
                    System.out.println("No enrollments found for this student.");
                }
            }
        }
    }
    
    // view operations - course
    
    private static void viewAllCourses(Connection conn) throws SQLException {
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.credits, " +
                     "i.first_name AS instructor_first, i.last_name AS instructor_last, " +
                     "cl.building, cl.room_number " +
                     "FROM Course c " +
                     "JOIN Instructor i ON c.instructor_id = i.instructor_id " +
                     "JOIN Classroom cl ON c.classroom_id = cl.classroom_id " +
                     "ORDER BY c.course_code";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\nAll Courses");
            System.out.printf("%-10s %-15s %-30s %-8s %-25s %-15s%n",
                "ID", "Code", "Name", "Credits", "Instructor", "Location");
            System.out.println("--------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-10d %-15s %-30s %-8d %-25s %-15s%n",
                    rs.getInt("course_id"),
                    rs.getString("course_code"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("instructor_first") + " " + rs.getString("instructor_last"),
                    rs.getString("building") + " " + rs.getString("room_number"));
            }
            
            if (!hasData) {
                System.out.println("No courses found.");
            }
        }
    }
    
    private static void viewCourseByCode(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter course code (e.g., CS157A): ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.credits, " +
                     "i.first_name AS instructor_first, i.last_name AS instructor_last, i.email AS instructor_email, " +
                     "cl.building, cl.room_number, cl.capacity " +
                     "FROM Course c " +
                     "JOIN Instructor i ON c.instructor_id = i.instructor_id " +
                     "JOIN Classroom cl ON c.classroom_id = cl.classroom_id " +
                     "WHERE c.course_code = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nCourse Details");
                    System.out.println("ID: " + rs.getInt("course_id"));
                    System.out.println("Code: " + rs.getString("course_code"));
                    System.out.println("Name: " + rs.getString("course_name"));
                    System.out.println("Credits: " + rs.getInt("credits"));
                    System.out.println("Instructor: " + rs.getString("instructor_first") + " " + rs.getString("instructor_last"));
                    System.out.println("Instructor Email: " + rs.getString("instructor_email"));
                    System.out.println("Location: " + rs.getString("building") + " " + rs.getString("room_number"));
                    System.out.println("Capacity: " + rs.getInt("capacity"));
                } else {
                    System.out.println("Course not found with code: " + courseCode);
                }
            }
        }
    }
    
    private static void viewCourseRoster(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter semester (e.g., Fall 2025): ");
        String semester = scanner.nextLine().trim();
        
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.email, e.grade " +
                     "FROM Enrollment e " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "WHERE c.course_code = ? AND e.semester = ? " +
                     "ORDER BY s.last_name, s.first_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            pstmt.setString(2, semester);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nRoster for " + courseCode + " (" + semester + ")");
                System.out.printf("%-10s %-20s %-20s %-30s %-10s%n",
                    "ID", "First Name", "Last Name", "Email", "Grade");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-10d %-20s %-20s %-30s %-10s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("grade") != null ? rs.getString("grade") : "N/A");
                }
                
                if (!hasData) {
                    System.out.println("No students enrolled in this course for the specified semester.");
                }
            }
        }
    }
    
    private static void viewCoursesByInstructor(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter instructor email: ");
        String email = scanner.nextLine().trim();
        
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.credits " +
                     "FROM Course c " +
                     "JOIN Instructor i ON c.instructor_id = i.instructor_id " +
                     "WHERE i.email = ? " +
                     "ORDER BY c.course_code";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nCourses by " + email);
                System.out.printf("%-10s %-15s %-30s %-8s%n",
                    "ID", "Code", "Name", "Credits");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-10d %-15s %-30s %-8d%n",
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"));
                }
                
                if (!hasData) {
                    System.out.println("No courses found for this instructor.");
                }
            }
        }
    }
    
    // view operations - enrollment
    
    private static void viewAllEnrollments(Connection conn) throws SQLException {
        String sql = "SELECT s.first_name, s.last_name, s.email, c.course_code, c.course_name, " +
                     "e.semester, e.grade " +
                     "FROM Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "ORDER BY e.semester, c.course_code, s.last_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("\nAll Enrollments");
            System.out.printf("%-20s %-30s %-15s %-30s %-15s %-10s%n",
                "Student Name", "Email", "Course Code", "Course Name", "Semester", "Grade");
            System.out.println("--------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-20s %-30s %-15s %-30s %-15s %-10s%n",
                    rs.getString("first_name") + " " + rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("course_code"),
                    rs.getString("course_name"),
                    rs.getString("semester"),
                    rs.getString("grade") != null ? rs.getString("grade") : "N/A");
            }
            
            if (!hasData) {
                System.out.println("No enrollments found.");
            }
        }
    }
    
    private static void viewEnrollmentsBySemester(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter semester (e.g., Fall 2025): ");
        String semester = scanner.nextLine().trim();
        
        String sql = "SELECT s.first_name, s.last_name, s.email, c.course_code, c.course_name, e.grade " +
                     "FROM Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "WHERE e.semester = ? " +
                     "ORDER BY c.course_code, s.last_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, semester);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nEnrollments for " + semester);
                System.out.printf("%-20s %-30s %-15s %-30s %-10s%n",
                    "Student Name", "Email", "Course Code", "Course Name", "Grade");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-20s %-30s %-15s %-30s %-10s%n",
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("grade") != null ? rs.getString("grade") : "N/A");
                }
                
                if (!hasData) {
                    System.out.println("No enrollments found for this semester.");
                }
            }
        }
    }
    
    private static void viewEnrollmentsByCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        String sql = "SELECT s.first_name, s.last_name, s.email, e.semester, e.grade " +
                     "FROM Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "WHERE c.course_code = ? " +
                     "ORDER BY e.semester, s.last_name";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nEnrollments for " + courseCode);
                System.out.printf("%-20s %-30s %-15s %-10s%n",
                    "Student Name", "Email", "Semester", "Grade");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-20s %-30s %-15s %-10s%n",
                        rs.getString("first_name") + " " + rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("semester"),
                        rs.getString("grade") != null ? rs.getString("grade") : "N/A");
                }
                
                if (!hasData) {
                    System.out.println("No enrollments found for this course.");
                }
            }
        }
    }
    
    private static void viewStudentTranscript(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        
        String sql = "SELECT semester, course_code, course_name, credits, grade, " +
                     "instructor_first_name, instructor_last_name " +
                     "FROM student_transcript_view " +
                     "WHERE email = ? " +
                     "ORDER BY semester, course_code";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nTranscript for " + email);
                System.out.printf("%-15s %-15s %-30s %-8s %-10s %-25s%n",
                    "Semester", "Course Code", "Course Name", "Credits", "Grade", "Instructor");
                System.out.println("--------------------------------------------------------------------------------");
                
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("%-15s %-15s %-30s %-8d %-10s %-25s%n",
                        rs.getString("semester"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("grade") != null ? rs.getString("grade") : "N/A",
                        rs.getString("instructor_first_name") + " " + rs.getString("instructor_last_name"));
                }
                
                if (!hasData) {
                    System.out.println("No transcript data found for this student.");
                }
            }
        }
    }
    
    // insert operations
    
    private static void addNewStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nAdd New Student");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter date of birth (YYYY-MM-DD) or press Enter to skip: ");
        String dobStr = scanner.nextLine().trim();
        
        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("Error: First name and last name cannot be empty.");
            return;
        }
        if (email.isEmpty()) {
            System.out.println("Error: Email cannot be empty.");
            return;
        }
        
        String sql = "INSERT INTO Student (first_name, last_name, email, dob) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            
            if (dobStr.isEmpty()) {
                pstmt.setNull(4, Types.DATE);
            } else {
                try {
                    java.sql.Date dob = java.sql.Date.valueOf(dobStr);
                    pstmt.setDate(4, dob);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid date format. Use YYYY-MM-DD");
                    return;
                }
            }
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added");
            } else {
                System.out.println("Failed to add student.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Unique constraint violation
                System.out.println("Error: Email already exists. Please use a different email.");
            } else {
                throw e;
            }
        }
    }
    
    private static void addNewCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nAdd New Course");
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
        System.out.print("Enter course code (e.g., CS157A): ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter credits (1-6): ");
        String creditsStr = scanner.nextLine().trim();
        System.out.print("Enter instructor ID: ");
        String instructorIdStr = scanner.nextLine().trim();
        System.out.print("Enter classroom ID: ");
        String classroomIdStr = scanner.nextLine().trim();
        
        // Validation
        if (courseName.isEmpty() || courseCode.isEmpty()) {
            System.out.println("Error: Course name and code cannot be empty.");
            return;
        }
        
        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits < 1 || credits > 6) {
                System.out.println("Error: Credits must be between 1 and 6.");
                return;
            }
            
            int instructorId = Integer.parseInt(instructorIdStr);
            int classroomId = Integer.parseInt(classroomIdStr);
            
            String sql = "INSERT INTO Course (course_name, course_code, credits, instructor_id, classroom_id) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, courseName);
                pstmt.setString(2, courseCode);
                pstmt.setInt(3, credits);
                pstmt.setInt(4, instructorId);
                pstmt.setInt(5, classroomId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Course added");
                } else {
                    System.out.println("Failed to add course.");
                }
            } catch (SQLException e) {
                if (e.getSQLState().equals("23000")) {
                    System.out.println("Error: Course code already exists.");
                } else if (e.getSQLState().equals("23000") || e.getSQLState().startsWith("23")) {
                    System.out.println("Error: Invalid instructor ID or classroom ID.");
                } else {
                    throw e;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Credits, instructor ID, and classroom ID must be valid integers.");
        }
    }
    
    private static void enrollStudentInCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nEnroll Student in Course");
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter semester (e.g., Fall 2025): ");
        String semester = scanner.nextLine().trim();
        System.out.print("Enter grade (or press Enter to leave blank): ");
        String grade = scanner.nextLine().trim().toUpperCase();
        
        // get student and course IDs
        String getStudentSql = "SELECT student_id FROM Student WHERE email = ?";
        String getCourseSql = "SELECT course_id FROM Course WHERE course_code = ?";
        
        int studentId = -1;
        int courseId = -1;
        
        try (PreparedStatement pstmt = conn.prepareStatement(getStudentSql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    studentId = rs.getInt("student_id");
                } else {
                    System.out.println("Error: Student not found.");
                    return;
                }
            }
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(getCourseSql)) {
            pstmt.setString(1, courseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    courseId = rs.getInt("course_id");
                } else {
                    System.out.println("Error: Course not found.");
                    return;
                }
            }
        }
        
        // validate grade
        if (!grade.isEmpty()) {
            String[] validGrades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
            boolean valid = false;
            for (String g : validGrades) {
                if (grade.equals(g)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Error: Invalid grade format. Valid grades: A, A-, B+, B, B-, C+, C, C-, D, F");
                return;
            }
        }
        
        // insert enrollment
        String sql = "INSERT INTO Enrollment (student_id, course_id, semester, grade) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            pstmt.setString(3, semester);
            if (grade.isEmpty()) {
                pstmt.setNull(4, Types.CHAR);
            } else {
                pstmt.setString(4, grade);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student enrolled");
            } else {
                System.out.println("Failed to enroll student.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.out.println("Error: Student is already enrolled in this course for this semester.");
            } else if (e.getSQLState().equals("45000")) {
                System.out.println("Error: " + e.getMessage()); // Trigger error message
            } else {
                throw e;
            }
        }
    }
    
    // update operations
    
    private static void updateStudentEmail(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nUpdate Student Email");
        System.out.print("Enter current student email: ");
        String currentEmail = scanner.nextLine().trim();
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine().trim();
        
        if (newEmail.isEmpty()) {
            System.out.println("Error: New email cannot be empty.");
            return;
        }
        
        String sql = "UPDATE Student SET email = ? WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setString(2, currentEmail);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Email updated");
            } else {
                System.out.println("Student not found with email: " + currentEmail);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                System.out.println("Error: New email already exists. Please use a different email.");
            } else {
                throw e;
            }
        }
    }
    
    private static void updateCourseCredits(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nUpdate Course Credits");
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter new credits (1-6): ");
        String creditsStr = scanner.nextLine().trim();
        
        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits < 1 || credits > 6) {
                System.out.println("Error: Credits must be between 1 and 6.");
                return;
            }
            
            String sql = "UPDATE Course SET credits = ? WHERE course_code = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, credits);
                pstmt.setString(2, courseCode);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Credits updated");
                } else {
                    System.out.println("Course not found with code: " + courseCode);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Credits must be a valid integer.");
        }
    }
    
    private static void updateGrade(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nUpdate Grade");
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter semester: ");
        String semester = scanner.nextLine().trim();
        System.out.print("Enter new grade (A, A-, B+, B, B-, C+, C, C-, D, F): ");
        String grade = scanner.nextLine().trim().toUpperCase();
        
        // validate grade
        String[] validGrades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        boolean valid = false;
        for (String g : validGrades) {
            if (grade.equals(g)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            System.out.println("Error: Invalid grade format. Valid grades: A, A-, B+, B, B-, C+, C, C-, D, F");
            return;
        }
        
        // update grade
        String sql = "UPDATE Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "SET e.grade = ? " +
                     "WHERE s.email = ? AND c.course_code = ? AND e.semester = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, grade);
            pstmt.setString(2, email);
            pstmt.setString(3, courseCode);
            pstmt.setString(4, semester);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Grade updated");
            } else {
                System.out.println("Enrollment not found. Please check student email, course code, and semester.");
            }
        }
    }
    
    // delete operations
    
    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nDelete Student");
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        
        String sql = "DELETE FROM Student WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted (enrollments also deleted)");
            } else {
                System.out.println("Student not found with email: " + email);
            }
        }
    }
    
    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nDelete Course");
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        
        String sql = "DELETE FROM Course WHERE course_code = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course deleted (enrollments also deleted)");
            } else {
                System.out.println("Course not found with code: " + courseCode);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000") || e.getSQLState().startsWith("23")) {
                System.out.println("Error: Cannot delete course due to foreign key constraints.");
            } else {
                throw e;
            }
        }
    }
    
    private static void dropEnrollment(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\nDrop Enrollment");
        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter semester: ");
        String semester = scanner.nextLine().trim();
        
        // delete enrollment
        String sql = "DELETE e FROM Enrollment e " +
                     "JOIN Student s ON e.student_id = s.student_id " +
                     "JOIN Course c ON e.course_id = c.course_id " +
                     "WHERE s.email = ? AND c.course_code = ? AND e.semester = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, courseCode);
            pstmt.setString(3, semester);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                    System.out.println("Enrollment dropped");
            } else {
                System.out.println("Enrollment not found. Please check student email, course code, and semester.");
            }
        }
    }
    
    // utility methods
    
    private static int getMenuChoice(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
                }
            }
            catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number between " + min + " and " + max + ": ");
            }
        }
    }
}