package com.cgpa.backend.service;

import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.model.Subject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CgpaServiceTest {
    @Test
    void mapsSupportedGradesToPointsCaseInsensitively() {
        assertEquals(10.0, CgpaService.gradeToPoint("O"));
        assertEquals(9.0, CgpaService.gradeToPoint(" a+ "));
        assertEquals(8.0, CgpaService.gradeToPoint("a"));
        assertEquals(7.0, CgpaService.gradeToPoint("B+"));
        assertEquals(6.0, CgpaService.gradeToPoint("B"));
        assertEquals(5.5, CgpaService.gradeToPoint("C+"));
        assertEquals(5.0, CgpaService.gradeToPoint("C"));
    }

    @Test
    void treatsFailUnknownAndNullGradesAsZeroPoints() {
        assertEquals(0.0, CgpaService.gradeToPoint("F"));
        assertEquals(0.0, CgpaService.gradeToPoint("A-"));
        assertEquals(0.0, CgpaService.gradeToPoint(null));
    }

    @Test
    void computesCreditWeightedCgpaAcrossSemesters() {
        SubjectDao dao = new SubjectDao() {
            @Override
            public List<Subject> findByStudentRoll(String roll) {
                return List.of(
                    new Subject(1, roll, "Math", "M1", 4, "O", 1),
                    new Subject(2, roll, "Physics", "P1", 2, "A", 1),
                    new Subject(3, roll, "Algorithms", "A1", 3, "B+", 2)
                );
            }
        };

        // (4*10 + 2*8 + 3*7) / (4+2+3) = 8.333...
        assertEquals(8.3333333333,
            new CgpaService(dao).computeCgpaForStudent("S001"), 0.000000001);
    }

    @Test
    void returnsZeroWhenStudentHasNoSubjects() {
        SubjectDao dao = new SubjectDao() {
            @Override
            public List<Subject> findByStudentRoll(String roll) {
                return List.of();
            }
        };
        assertEquals(0.0, new CgpaService(dao).computeCgpaForStudent("S002"));
    }
}
