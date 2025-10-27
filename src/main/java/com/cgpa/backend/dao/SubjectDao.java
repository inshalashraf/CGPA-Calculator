package com.cgpa.backend.dao;

import com.cgpa.backend.model.Subject;
import com.cgpa.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDao {
    public Subject insert(Subject subject) {
        String sql = "INSERT INTO subjects(student_roll, subject_name, subject_code, credits, grade, semester) VALUES(?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, subject.getStudentRoll());
            ps.setString(2, subject.getSubjectName());
            ps.setString(3, subject.getSubjectCode());
            ps.setInt(4, subject.getCredits());
            ps.setString(5, subject.getGrade());
            ps.setInt(6, subject.getSemester());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) subject.setId(rs.getInt(1));
            }
            return subject;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Subject subject) {
        String sql = "UPDATE subjects SET student_roll=?, subject_name=?, subject_code=?, credits=?, grade=?, semester=? WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getStudentRoll());
            ps.setString(2, subject.getSubjectName());
            ps.setString(3, subject.getSubjectCode());
            ps.setInt(4, subject.getCredits());
            ps.setString(5, subject.getGrade());
            ps.setInt(6, subject.getSemester());
            ps.setInt(7, subject.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM subjects WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subject> findByStudentRoll(String roll) {
        String sql = "SELECT id, student_roll, subject_name, subject_code, credits, grade, semester FROM subjects WHERE student_roll=? ORDER BY id DESC";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            List<Subject> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subject> findAll() {
        String sql = "SELECT id, student_roll, subject_name, subject_code, credits, grade, semester FROM subjects ORDER BY id DESC";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Subject> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Subject map(ResultSet rs) throws SQLException {
        return new Subject(
                rs.getInt("id"),
                rs.getString("student_roll"),
                rs.getString("subject_name"),
                rs.getString("subject_code"),
                rs.getInt("credits"),
                rs.getString("grade"),
                rs.getInt("semester")
        );
    }
}

