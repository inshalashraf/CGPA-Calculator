-- Schema for GradeHub's example database (cgpa_db)
-- Creates database, tables and sample data for students and subjects
-- Run these statements in MySQL to create the database and tables.

-- 1) Create database
CREATE DATABASE IF NOT EXISTS `cgpa_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `cgpa_db`;

-- 2) Create students table
-- Columns inferred from attachment: roll_number, name, email, department, section, batch, id
-- We'll use `roll_number` as the natural primary key and keep an auto-increment `id` as internal id if needed.
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `roll_number` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) DEFAULT NULL,
  `department` VARCHAR(100) DEFAULT NULL,
  `section` VARCHAR(5) DEFAULT NULL,
  `batch` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_students_roll` (`roll_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) Create subjects table
-- Columns inferred from attachment: id (auto_increment), student_roll (varchar(50)), subject_name, subject_code, credits, grade, semester
DROP TABLE IF EXISTS `subjects`;
CREATE TABLE `subjects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `student_roll` VARCHAR(50) NOT NULL,
  `subject_name` VARCHAR(150) NOT NULL,
  `subject_code` VARCHAR(50) DEFAULT NULL,
  `credits` INT NOT NULL DEFAULT 0,
  `grade` VARCHAR(5) DEFAULT NULL,
  `semester` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_subjects_student_roll` (`student_roll`),
  CONSTRAINT `fk_subjects_student` FOREIGN KEY (`student_roll`) REFERENCES `students`(`roll_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- NOTE: The foreign key references `students.roll_number`, which is a UNIQUE column (not the PK id).
-- Ensure your connector and code use the same roll_number values.

-- 4) Sample data (optional)
INSERT INTO `students` (`roll_number`, `name`, `email`, `department`, `section`, `batch`) VALUES
('R2024001', 'Alice Smith', 'alice@example.edu', 'Computer Science', 'A', 2024),
('R2024002', 'Bob Patel', 'bob@example.edu', 'Electronics', 'B', 2024);

INSERT INTO `subjects` (`student_roll`, `subject_name`, `subject_code`, `credits`, `grade`, `semester`) VALUES
('R2024001', 'Data Structures', 'CS201', 4, 'A', 1),
('R2024001', 'Discrete Mathematics', 'CS101', 3, 'B+', 1),
('R2024002', 'Circuits', 'EC201', 4, 'A-', 1);

-- 5) Helpful queries
-- Get student GPA/credits summary (example; adapt CGPA logic as needed):
-- SELECT s.roll_number, s.name, SUM(sub.credits) AS total_credits, SUM(sub.credits * CASE sub.grade
--   WHEN 'A' THEN 4.0 WHEN 'A-' THEN 3.7 WHEN 'B+' THEN 3.3 WHEN 'B' THEN 3.0 WHEN 'B-' THEN 2.7
--   WHEN 'C+' THEN 2.3 WHEN 'C' THEN 2.0 WHEN 'D' THEN 1.0 WHEN 'F' THEN 0.0 ELSE 0 END) / SUM(sub.credits) AS cgpa
-- FROM students s
-- JOIN subjects sub ON sub.student_roll = s.roll_number
-- GROUP BY s.roll_number, s.name;

-- End of schema file
