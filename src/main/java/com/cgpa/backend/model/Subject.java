package com.cgpa.backend.model;

public class Subject {
    private Integer id;
    private String studentRoll;
    private String subjectName;
    private String subjectCode;
    private int credits;
    private String grade; // e.g., A+, A, B, etc.
    private int semester;

    public Subject() {}

    public Subject(Integer id, String studentRoll, String subjectName, String subjectCode, int credits, String grade, int semester) {
        this.id = id;
        this.studentRoll = studentRoll;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
        this.grade = grade;
        this.semester = semester;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getStudentRoll() { return studentRoll; }
    public void setStudentRoll(String studentRoll) { this.studentRoll = studentRoll; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}

