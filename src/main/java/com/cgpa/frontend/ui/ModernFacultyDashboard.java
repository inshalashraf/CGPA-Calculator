package com.cgpa.frontend.ui;

import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.service.CgpaService;
import com.cgpa.frontend.ui.components.CgpaPanel;
import com.cgpa.backend.model.Student;
import com.cgpa.backend.model.Subject;
import com.cgpa.frontend.events.StudentEventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class ModernFacultyDashboard extends JPanel {
    private final JComboBox<String> sectionCombo = new JComboBox<>(buildSections());
    private final JComboBox<Integer> batchCombo = new JComboBox<>(buildBatches());
    private final JComboBox<Student> studentCombo = new JComboBox<>();
    private final JLabel cgpaLabel = new JLabel("0.00");
    private final JLabel classificationLabel = new JLabel("N/A");
    private final StudentDao studentDao = new StudentDao();
    private final CgpaService cgpaService = new CgpaService(new SubjectDao());
    private final SubjectDao subjectDao = new SubjectDao();
    private JFrame parentFrame;
    
    // Chart data
    private Map<Integer, Double> semesterGpa = new HashMap<>();
    private double overallCgpa = 0.0;
    private boolean hasData = false;
    
    // Colors for modern theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);

    public ModernFacultyDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255, 0)); // Transparent
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);
        
        createHeader();
        createMainContent();
        setupListeners();
        refreshStudents();
        StudentEventBus.register(this::refreshStudents);
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle background
                int arc = 20;
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                // Add gradient overlay
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(231, 76, 60, 30),
                    getWidth(), 0, new Color(192, 57, 43, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                // Add subtle border
                g2d.setColor(new Color(231, 76, 60, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        headerPanel.setLayout(new BorderLayout(20, 20));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        headerPanel.setPreferredSize(new Dimension(0, 120));
        
        // Left side with icon and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("👨‍🏫");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Faculty Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(40, 40, 40));
        
        JLabel subtitleLabel = new JLabel("Access student information and calculate CGPA");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(80, 80, 80));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(titlePanel);
        
        // Right side with quick stats
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        // Add some quick stats cards
        rightPanel.add(createQuickStatCard("📊", "Analytics", "View"));
        rightPanel.add(createQuickStatCard("👥", "Students", "Manage"));
        rightPanel.add(createQuickStatCard("📈", "Reports", "Generate"));
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private JPanel createQuickStatCard(String emoji, String title, String action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle
                int arc = 15;
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                // Add subtle border
                g2d.setColor(new Color(231, 76, 60, 80));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(80, 70));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        emojiLabel.setForeground(Color.BLACK);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel actionLabel = new JLabel(action);
        actionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        actionLabel.setForeground(new Color(100, 100, 100));
        actionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(emojiLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(actionLabel);
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setPreferredSize(new Dimension(85, 75));
                card.revalidate();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setPreferredSize(new Dimension(80, 70));
                card.revalidate();
            }
        });
        
        return card;
    }
    
    private void createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        
        // Filter Section
        mainPanel.add(createFilterCard(), BorderLayout.NORTH);
        
        // Charts Section
        mainPanel.add(createChartsPanel(), BorderLayout.CENTER);
        
        // CGPA Results Section
        mainPanel.add(createResultsPanel(), BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFilterCard() {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Student Filter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        card.add(titleLabel, gbc);
        
        // Section and Batch selection
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; card.add(new JLabel("Section:"), gbc);
        gbc.gridx = 1; card.add(sectionCombo, gbc);
        gbc.gridx = 2; card.add(new JLabel("Batch:"), gbc);
        gbc.gridx = 3; card.add(batchCombo, gbc);
        
        // Student selection
        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(studentCombo, gbc);
        gbc.gridwidth = 1;
        
        // Calculate Button
        JButton calcBtn = new JButton("Calculate CGPA");
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setBackground(ACCENT_COLOR);
        calcBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        calcBtn.setFocusPainted(false);
        calcBtn.addActionListener(e -> calculate());
        gbc.gridx = 3; gbc.gridy = 2;
        card.add(calcBtn, gbc);
        
        return card;
    }
    
    private JPanel createChartsPanel() {
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
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        PieChartPanel pieChart = new PieChartPanel(isLastTwoSemesters);
        pieChart.setPreferredSize(new Dimension(200, 200));
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(pieChart, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createResultsPanel() {
        JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        results.setOpaque(false);
        
        JLabel cgpaTextLabel = new JLabel("CGPA:");
        cgpaTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cgpaTextLabel.setForeground(TEXT_COLOR);
        
        cgpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        cgpaLabel.setForeground(SUCCESS_COLOR);
        cgpaLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        classificationLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        classificationLabel.setForeground(new Color(52, 152, 219));
        
        results.add(cgpaTextLabel);
        results.add(cgpaLabel);
        results.add(classificationLabel);
        
        return results;
    }
    
    private void setupListeners() {
        sectionCombo.addActionListener(e -> refreshStudents());
        batchCombo.addActionListener(e -> refreshStudents());
    }

    private void refreshStudents() {
        studentCombo.removeAllItems();
        String selectedSection = (String) sectionCombo.getSelectedItem();
        Integer selectedBatch = (Integer) batchCombo.getSelectedItem();
        if (selectedSection != null && selectedBatch != null) {
            List<Student> students = studentDao.findBySectionAndBatch(selectedSection, selectedBatch);
            for (Student s : students) {
                studentCombo.addItem(s);
            }
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

    private static String[] buildSections() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (char c = 'A'; c <= 'T'; c++) {
            list.add(c + "1");
        }
        for (char c = 'A'; c <= 'T'; c++) {
            list.add(c + "2");
        }
        return list.toArray(new String[0]);
    }

    private static Integer[] buildBatches() {
        return new Integer[]{2023, 2024, 2025};
    }
    
    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
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