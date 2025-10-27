package com.cgpa.backend.dao;

import com.cgpa.backend.model.Student;
import com.cgpa.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public Student insert(Student student) {
        String sql = "INSERT INTO students(name, email, roll_number, department, section, batch) VALUES(?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getRollNumber());
            ps.setString(4, student.getDepartment());
            ps.setString(5, student.getSection());
            ps.setInt(6, student.getBatch());
            ps.executeUpdate();
            return student;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Student student) {
        String sql = "UPDATE students SET name=?, email=?, department=?, section=?, batch=? WHERE roll_number=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getDepartment());
            ps.setString(4, student.getSection());
            ps.setInt(5, student.getBatch());
            ps.setString(6, student.getRollNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String rollNumber) {
        String sql = "DELETE FROM students WHERE roll_number=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rollNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT name, email, roll_number, department, section, batch FROM students ORDER BY roll_number DESC";
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            List<Student> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Student findByRoll(String roll) {
        String sql = "SELECT name, email, roll_number, department, section, batch FROM students WHERE roll_number=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> findBySection(String section) {
        String sql = "SELECT name, email, roll_number, department, section, batch FROM students WHERE section=? ORDER BY roll_number";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section);
            List<Student> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> findBySectionAndBatch(String section, int batch) {
        String sql = "SELECT name, email, roll_number, department, section, batch FROM students WHERE section=? AND batch=? ORDER BY roll_number";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section);
            ps.setInt(2, batch);
            List<Student> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("roll_number"),
                rs.getString("department"),
                rs.getString("section"),
                rs.getInt("batch")
        );
    }
}

