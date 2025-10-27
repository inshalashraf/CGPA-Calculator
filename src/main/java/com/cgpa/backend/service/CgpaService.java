package com.cgpa.backend.service;

import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.model.Subject;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class CgpaService {
    private final SubjectDao subjectDao;

    public CgpaService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public static double gradeToPoint(String grade) {
        if (grade == null) return 0.0;
        return switch (grade.trim().toUpperCase()) {
            case "O" -> 10.0;
            case "A+" -> 9.0;
            case "A" -> 8.0;
            case "B+" -> 7.0;
            case "B" -> 6.0;
            case "C+" -> 5.5; // Not in image, but keeping for completeness
            case "C" -> 5.0;
            default -> 0.0; // F or unknown
        };
    }

    public double computeCgpaForStudent(String studentRoll) {
        List<Subject> subjects = subjectDao.findByStudentRoll(studentRoll);
        if (subjects.isEmpty()) {
            return 0.0;
        }
        
        // Group subjects by semester
        Map<Integer, List<Subject>> semesterSubjects = new HashMap<>();
        for (Subject subject : subjects) {
            semesterSubjects.computeIfAbsent(subject.getSemester(), k -> new ArrayList<>()).add(subject);
        }
        
        double totalSemesterCredits = 0.0;
        double weightedSgpaSum = 0.0;
        
        // Calculate SGPA for each semester and then CGPA
        for (Map.Entry<Integer, List<Subject>> entry : semesterSubjects.entrySet()) {
            List<Subject> semesterSubs = entry.getValue();
            
            double semesterCredits = 0.0;
            double semesterWeightedSum = 0.0;
            
            // Calculate SGPA for this semester: (Σ Credits × Grade Points) / (Σ Credits)
            for (Subject sub : semesterSubs) {
                double points = gradeToPoint(sub.getGrade());
                semesterCredits += sub.getCredits();
                semesterWeightedSum += points * sub.getCredits();
            }
            
            if (semesterCredits > 0) {
                double sgpa = semesterWeightedSum / semesterCredits;
                
                // Add to CGPA calculation: (Σ Semester Credits × SGPA) / (Σ Semester Credits)
                totalSemesterCredits += semesterCredits;
                weightedSgpaSum += semesterCredits * sgpa;
            }
        }
        
        if (totalSemesterCredits == 0) return 0.0;
        
        // CGPA = (Σ Semester Credits × SGPA) / (Σ Semester Credits)
        return weightedSgpaSum / totalSemesterCredits;
    }
}

