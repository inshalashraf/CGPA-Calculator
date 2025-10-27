package com.cgpa.backend.model;

public class Student {
    private String name;
    private String email;
    private String rollNumber; // primary identifier
    private String department;
    private String section; // e.g., A1..T2
    private int batch; // e.g., 2024

    public Student() {}

    public Student(String name, String email, String rollNumber, String department, String section, int batch) {
        this.name = name;
        this.email = email;
        this.rollNumber = rollNumber;
        this.department = department;
        this.section = section;
        this.batch = batch;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public int getBatch() { return batch; }
    public void setBatch(int batch) { this.batch = batch; }

    @Override
    public String toString() {
        String roll = rollNumber == null ? "" : rollNumber;
        String nm = name == null ? "" : name;
        return nm + " (" + roll + ")";
    }
}

