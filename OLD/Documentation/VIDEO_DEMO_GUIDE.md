# Video Demo Guide - Transactional Workflow

## Overview
This guide explains how to demonstrate the transactional workflow (COMMIT/ROLLBACK) in your ≤ 6 minute video.

## Menu Location
**Main Menu → Option 5: Transactional Workflow (Multi-Course Enrollment)**

## What the Workflow Does
The transactional workflow enrolls a student in multiple courses atomically. It touches multiple tables:
- **Student table**: Validates student exists
- **Course table**: Validates each course exists  
- **Enrollment table**: Inserts enrollment records

**Key Behavior:**
- If ALL enrollments succeed → **COMMIT** (all changes become permanent)
- If ANY enrollment fails → **ROLLBACK** (all changes are undone, database restored)

## Video Demo Structure

### Part 1: Successful Transaction (COMMIT) - ~2-3 minutes

**Setup:**
1. Start the application
2. Navigate to Main Menu → Option 5

**Demonstration Steps:**
1. **Explain the workflow:**
   - "This workflow will enroll a student in multiple courses atomically"
   - "If all succeed, we'll COMMIT. If any fail, we'll ROLLBACK"

2. **Enter inputs:**
   - Student email: Use an existing student (e.g., `aisha.khan@sjsu.edu`)
   - Semester: `Spring 2027` (new semester to avoid conflicts)
   - Course codes: `CS149,MATH161` (two valid courses)

3. **Show the transaction process:**
   - Point out: ">>> Transaction started. Auto-commit disabled."
   - Watch as each course is validated
   - Show the checkmarks (✓) for successful validations
   - Show "✓ Enrollment inserted successfully" for each course

4. **Show COMMIT:**
   - Point out the message: ">>> ALL ENROLLMENTS SUCCEEDED"
   - Highlight: ">>> Committing transaction..."
   - Emphasize: ">>> Transaction succeeded. Committing changes."
   - Show: ">>> Commit complete. All enrollments are now permanent in the database."

5. **Verify in database (optional but recommended):**
   - Use View Operations → View Student Enrollments
   - Show that both enrollments exist in the database

---

### Part 2: Failed Transaction (ROLLBACK) - ~2-3 minutes

**Setup:**
1. Stay in the application (or restart if needed)
2. Navigate to Main Menu → Option 5 again

**Demonstration Steps:**
1. **Explain the failure scenario:**
   - "Now I'll demonstrate ROLLBACK by intentionally causing a failure"
   - "I'll try to enroll a student in CS157A, which already has 2 students enrolled"
   - "The classroom capacity is 2, so adding a third will trigger the capacity check"

2. **Enter inputs:**
   - Student email: `sarah.lee@sjsu.edu` (or any student not in CS157A Fall 2025)
   - Semester: `Fall 2025` (existing semester where CS157A is at capacity)
   - Course codes: `CS157A,MATH161` (CS157A will fail due to capacity)

3. **Show the transaction process:**
   - Point out: ">>> Transaction started. Auto-commit disabled."
   - Show CS157A validation succeeds
   - Show duplicate check passes
   - **Watch for the failure:**
     - Show: "✗ ERROR: Enrollment blocked: classroom capacity exceeded"
     - Explain: "The trigger detected capacity exceeded"

4. **Show ROLLBACK:**
   - Point out: ">>> TRANSACTION FAILED"
   - Highlight: ">>> Rolling back all changes..."
   - Emphasize: ">>> Rollback complete. No enrollments were created."
   - Show: ">>> Database state restored to before transaction started."
   - **Important:** Point out that even though MATH161 was valid, it was also rolled back

5. **Verify in database (optional but recommended):**
   - Use View Operations → View Student Enrollments
   - Show that NO new enrollments were created
   - Emphasize atomicity: "All or nothing"

---

## Key Points to Emphasize

### During COMMIT Demo:
- ✓ "Transaction started - auto-commit is disabled"
- ✓ "All validations passed"
- ✓ "All enrollments inserted successfully"
- ✓ **"COMMIT makes all changes permanent"**
- ✓ "Database now contains all enrollments"

### During ROLLBACK Demo:
- ✗ "Transaction started - auto-commit is disabled"
- ✗ "One enrollment failed (capacity exceeded)"
- ✗ **"ROLLBACK undoes ALL changes, even the successful ones"**
- ✗ "Database restored to original state"
- ✗ "No partial enrollments - atomicity maintained"

---

## Visual Cues in Console Output

### COMMIT Path:
```
>>> Transaction started. Auto-commit disabled.
✓ Student validated: ...
✓ Semester validated: ...
✓ Course codes parsed: ...
>>> Processing enrollments...
  ✓ Course validated: ...
  ✓ Duplicate check passed
  ✓ Enrollment inserted successfully
>>> ALL ENROLLMENTS SUCCEEDED
>>> Committing transaction...
>>> Transaction succeeded. Committing changes.
>>> Commit complete. All enrollments are now permanent in the database.
```

### ROLLBACK Path:
```
>>> Transaction started. Auto-commit disabled.
✓ Student validated: ...
✓ Semester validated: ...
✓ Course codes parsed: ...
>>> Processing enrollments...
  ✓ Course validated: ...
  ✓ Duplicate check passed
  ✗ ERROR: Enrollment blocked: classroom capacity exceeded
>>> TRANSACTION FAILED
>>> Rolling back all changes...
>>> Rollback complete. No enrollments were created.
>>> Database state restored to before transaction started.
```

---

## Tips for Recording

1. **Clear Console:** Start with a clean console window
2. **Zoom Text:** Make sure console text is large enough to read
3. **Pause for Reading:** Give viewers time to read the messages
4. **Highlight Key Messages:** Use cursor or annotation to point out COMMIT/ROLLBACK messages
5. **Show Both Paths:** Make sure to demonstrate both success and failure scenarios
6. **Verify Results:** Optionally show database verification after each scenario

---

## Summary Checklist

- [ ] Part 1: Successful transaction with COMMIT
  - [ ] Show transaction start
  - [ ] Show all validations passing
  - [ ] Show COMMIT message clearly
  - [ ] Verify enrollments in database (optional)

- [ ] Part 2: Failed transaction with ROLLBACK
  - [ ] Show transaction start
  - [ ] Show failure (capacity exceeded)
  - [ ] Show ROLLBACK message clearly
  - [ ] Emphasize atomicity (all or nothing)
  - [ ] Verify no enrollments in database (optional)

- [ ] Total time: ≤ 6 minutes
- [ ] Both COMMIT and ROLLBACK clearly demonstrated
- [ ] Multiple tables touched (Student, Course, Enrollment)
- [ ] PreparedStatement usage evident (all SQL uses ? placeholders)

---

## Example Script (Optional)

**Opening:**
"Now I'll demonstrate the transactional workflow. This operation enrolls a student in multiple courses atomically, touching the Student, Course, and Enrollment tables. If all succeed, we COMMIT. If any fail, we ROLLBACK."

**COMMIT Demo:**
"I'll enroll aisha.khan in CS149 and MATH161 for Spring 2027. Watch as the transaction processes each enrollment... All succeed, so we COMMIT. The changes are now permanent."

**ROLLBACK Demo:**
"Now I'll demonstrate ROLLBACK by trying to enroll in CS157A, which is at capacity. Watch what happens... The enrollment fails, so we ROLLBACK. Notice that even though MATH161 was valid, it was also rolled back - this demonstrates atomicity."

**Closing:**
"This demonstrates how transactions ensure data integrity - either all operations succeed together, or none of them do."

