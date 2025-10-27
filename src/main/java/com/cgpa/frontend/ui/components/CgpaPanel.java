package com.cgpa.frontend.ui.components;

import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.model.Student;
import com.cgpa.backend.model.Subject;
import com.cgpa.backend.service.CgpaService;
import com.cgpa.frontend.events.StudentEventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class CgpaPanel extends JPanel {
    private final JComboBox<Student> studentCombo = new JComboBox<>();
    private final JLabel cgpaLabel = new JLabel("0.00");
    private final JLabel classificationLabel = new JLabel("N/A");
    
    // Chart data
    private Map<Integer, Double> semesterGpa = new HashMap<>();
    private double overallCgpa = 0.0;
    private boolean hasData = false;

    private final StudentDao studentDao;
    private final SubjectDao subjectDao;
    private final CgpaService cgpaService;

    public CgpaPanel(StudentDao studentDao, CgpaService service) {
        this.studentDao = studentDao;
        this.cgpaService = service;
        this.subjectDao = new SubjectDao();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(255, 255, 255, 0));
        setOpaque(false);
        
        add(buildHeader(), BorderLayout.NORTH);
        add(buildChartsPanel(), BorderLayout.CENTER);
        add(buildResults(), BorderLayout.SOUTH);
        refreshStudents();
        StudentEventBus.register(this::refreshStudents);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        header.setOpaque(false);
        
        JLabel studentLabel = new JLabel("Student:");
        studentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentLabel.setForeground(new Color(52, 73, 94));
        
        studentCombo.setPreferredSize(new Dimension(300, 35));
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton calcBtn = new JButton("Calculate CGPA");
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setBackground(new Color(52, 152, 219));
        calcBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        calcBtn.setFocusPainted(false);
        calcBtn.addActionListener(e -> calculate());
        
        header.add(studentLabel);
        header.add(studentCombo);
        header.add(calcBtn);
        
        return header;
    }

    private JPanel buildChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        chartsPanel.setOpaque(false);
        
        // SGPA Chart 1 (Last 2 semesters)
        chartsPanel.add(createChartPanel("SGPA - Last 2 Semesters", true));
        
        // SGPA Chart 2 (Individual semesters)
        chartsPanel.add(createChartPanel("SGPA - Individual Semesters", false));
        
        // Overall CGPA Chart
        chartsPanel.add(createChartPanel("Overall CGPA", false));
        
        return chartsPanel;
    }

    private JPanel createChartPanel(String title, boolean isLastTwoSemesters) {
        JPanel chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBackground(new Color(255, 255, 255, 220));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        chartPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        PieChartPanel pieChart = new PieChartPanel(isLastTwoSemesters);
        pieChart.setPreferredSize(new Dimension(200, 200));
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(pieChart, BorderLayout.CENTER);
        
        return chartPanel;
    }

    private JPanel buildResults() {
        JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        results.setOpaque(false);
        
        JLabel cgpaTextLabel = new JLabel("CGPA:");
        cgpaTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cgpaTextLabel.setForeground(new Color(52, 73, 94));
        
        cgpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        cgpaLabel.setForeground(new Color(46, 204, 113));
        cgpaLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        classificationLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        classificationLabel.setForeground(new Color(52, 152, 219));
        
        results.add(cgpaTextLabel);
        results.add(cgpaLabel);
        results.add(classificationLabel);
        
        return results;
    }

    private void refreshStudents() {
        studentCombo.removeAllItems();
        for (Student s : studentDao.findAll()) {
            studentCombo.addItem(s);
        }
    }

    private void calculate() {
        Student s = (Student) studentCombo.getSelectedItem();
        if (s == null || s.getRollNumber() == null) {
            JOptionPane.showMessageDialog(this, "Select a student");
            return;
        }
        
        // Calculate overall CGPA
        overallCgpa = cgpaService.computeCgpaForStudent(s.getRollNumber());
        
        // Calculate semester-wise GPA
        calculateSemesterGpa(s.getRollNumber());
        
        hasData = true;
        
        // Update display
        DecimalFormat df = new DecimalFormat("0.00");
        cgpaLabel.setText(df.format(overallCgpa));
        classificationLabel.setText(classify(overallCgpa));
        
        // Refresh charts
        repaint();
    }

    private void calculateSemesterGpa(String rollNumber) {
        semesterGpa.clear();
        List<Subject> subjects = subjectDao.findByStudentRoll(rollNumber);
        
        Map<Integer, List<Subject>> semesterSubjects = new HashMap<>();
        for (Subject subject : subjects) {
            semesterSubjects.computeIfAbsent(subject.getSemester(), k -> new ArrayList<>()).add(subject);
        }
        
        for (Map.Entry<Integer, List<Subject>> entry : semesterSubjects.entrySet()) {
            int semester = entry.getKey();
            List<Subject> semesterSubs = entry.getValue();
            
            double totalCredits = 0.0;
            double weightedSum = 0.0;
            
            for (Subject sub : semesterSubs) {
                double points = CgpaService.gradeToPoint(sub.getGrade());
                totalCredits += sub.getCredits();
                weightedSum += points * sub.getCredits();
            }
            
            if (totalCredits > 0) {
                // Calculate SGPA for this semester: (Σ Credits × Grade Points) / (Σ Credits)
                double sgpa = weightedSum / totalCredits;
                semesterGpa.put(semester, sgpa);
            }
        }
    }

    private String classify(double cgpa) {
        if (cgpa >= 9) return "Outstanding";
        if (cgpa >= 8) return "Excellent";
        if (cgpa >= 7) return "Very Good";
        if (cgpa >= 6) return "Good";
        if (cgpa >= 5) return "Pass";
        return "Fail";
    }

    // Custom Pie Chart Panel
    private class PieChartPanel extends JPanel {
        private final boolean isLastTwoSemesters;
        
        public PieChartPanel(boolean isLastTwoSemesters) {
            this.isLastTwoSemesters = isLastTwoSemesters;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!hasData) {
                drawNoDataMessage(g);
                return;
            }
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 20;
            
            if (isLastTwoSemesters) {
                drawLastTwoSemestersChart(g2d, centerX, centerY, radius);
            } else if (getTitle().contains("Individual")) {
                drawIndividualSemestersChart(g2d, centerX, centerY, radius);
            } else {
                drawOverallCgpaChart(g2d, centerX, centerY, radius);
            }
            
            g2d.dispose();
        }
        
        private void drawNoDataMessage(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            String message = "Click Calculate CGPA";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2;
            
            g2d.drawString(message, x, y);
            g2d.dispose();
        }
        
        private void drawLastTwoSemestersChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            List<Map.Entry<Integer, Double>> sortedSemesters = semesterGpa.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByKey().reversed())
                .limit(2)
                .toList();
            
            if (sortedSemesters.isEmpty()) return;
            
            double total = sortedSemesters.stream().mapToDouble(Map.Entry::getValue).sum();
            double startAngle = 0;
            
            Color[] colors = {new Color(52, 152, 219), new Color(231, 76, 60)};
            int colorIndex = 0;
            
            for (Map.Entry<Integer, Double> entry : sortedSemesters) {
                double percentage = entry.getValue() / total;
                double sweepAngle = percentage * 360;
                
                g2d.setColor(colors[colorIndex % colors.length]);
                g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                    radius * 2, radius * 2, startAngle, sweepAngle, Arc2D.PIE));
                
                // Draw semester label
                drawPieLabel(g2d, centerX, centerY, radius, startAngle + sweepAngle/2, 
                    "Sem " + entry.getKey() + "\n" + String.format("%.2f", entry.getValue()));
                
                startAngle += sweepAngle;
                colorIndex++;
            }
        }
        
        private void drawIndividualSemestersChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            if (semesterGpa.isEmpty()) return;
            
            double total = semesterGpa.values().stream().mapToDouble(Double::doubleValue).sum();
            double startAngle = 0;
            
            Color[] colors = {new Color(52, 152, 219), new Color(231, 76, 60), 
                             new Color(46, 204, 113), new Color(155, 89, 182),
                             new Color(241, 196, 15), new Color(230, 126, 34)};
            int colorIndex = 0;
            
            for (Map.Entry<Integer, Double> entry : semesterGpa.entrySet()) {
                double percentage = entry.getValue() / total;
                double sweepAngle = percentage * 360;
                
                g2d.setColor(colors[colorIndex % colors.length]);
                g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                    radius * 2, radius * 2, startAngle, sweepAngle, Arc2D.PIE));
                
                // Draw semester label
                drawPieLabel(g2d, centerX, centerY, radius, startAngle + sweepAngle/2, 
                    "Sem " + entry.getKey() + "\n" + String.format("%.2f", entry.getValue()));
                
                startAngle += sweepAngle;
                colorIndex++;
            }
        }
        
        private void drawOverallCgpaChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            if (overallCgpa == 0) return;
            
            // Create a donut chart showing CGPA out of 10
            double percentage = overallCgpa / 10.0;
            double sweepAngle = percentage * 360;
            
            // Background circle (gray)
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
            
            // CGPA arc (blue)
            g2d.setColor(new Color(52, 152, 219));
            g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                radius * 2, radius * 2, -90, sweepAngle, Arc2D.PIE));
            
            // Center text
            g2d.setColor(new Color(52, 73, 94));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            String cgpaText = String.format("%.2f", overallCgpa);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(cgpaText);
            int x = centerX - textWidth / 2;
            int y = centerY + fm.getAscent() / 2;
            g2d.drawString(cgpaText, x, y);
        }
        
        private void drawPieLabel(Graphics2D g2d, int centerX, int centerY, int radius, 
                                 double angle, String text) {
            double radian = Math.toRadians(angle);
            int labelRadius = radius * 3 / 4;
            int x = centerX + (int) (labelRadius * Math.cos(radian));
            int y = centerY - (int) (labelRadius * Math.sin(radian));
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
            
            String[] lines = text.split("\n");
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();
            
            for (int i = 0; i < lines.length; i++) {
                int textWidth = fm.stringWidth(lines[i]);
                int textX = x - textWidth / 2;
                int textY = y - (lines.length - 1) * lineHeight / 2 + i * lineHeight;
                g2d.drawString(lines[i], textX, textY);
            }
        }
        
        private String getTitle() {
            Container parent = getParent();
            if (parent instanceof JPanel) {
                Component[] components = ((JPanel) parent).getComponents();
                for (Component comp : components) {
                    if (comp instanceof JLabel) {
                        return ((JLabel) comp).getText();
                    }
                }
            }
            return "";
        }
    }
}

