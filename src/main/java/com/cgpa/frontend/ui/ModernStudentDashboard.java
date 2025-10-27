package com.cgpa.frontend.ui;

import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.service.CgpaService;
import com.cgpa.frontend.ui.components.*;
import javax.swing.*;
import java.awt.*;

public class ModernStudentDashboard extends JPanel {
    private JFrame parentFrame;
    private JTabbedPane tabs;
    
    // Enhanced colors for modern theme
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_DARK = new Color(41, 128, 185);
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color GRADIENT_START = new Color(52, 152, 219);
    private static final Color GRADIENT_END = new Color(41, 128, 185);
    
    public ModernStudentDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255, 0)); // Transparent
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);
        
        createHeader();
        createTabs();
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
                    0, 0, new Color(52, 152, 219, 30),
                    getWidth(), 0, new Color(41, 128, 185, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                // Add subtle border
                g2d.setColor(new Color(52, 152, 219, 100));
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
        
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(40, 40, 40));
        
        JLabel subtitleLabel = new JLabel("Manage your academic information and calculate CGPA");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(80, 80, 80));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        leftPanel.add(iconLabel);
        leftPanel.add(titlePanel);
        
        // Right side with stats or quick info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        // Add some quick stats cards
        rightPanel.add(createQuickStatCard("📚", "Subjects", "Manage"));
        rightPanel.add(createQuickStatCard("📊", "CGPA", "Calculate"));
        rightPanel.add(createQuickStatCard("👤", "Profile", "Update"));
        
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
                g2d.setColor(new Color(52, 152, 219, 80));
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
        
        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setForeground(Color.BLACK);

        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel actionLabel = new JLabel(action);
        actionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        actionLabel.setForeground(new Color(100, 100, 100));
        actionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
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
    
    private void createTabs() {
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent white
        tabs.setOpaque(false);
        
        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();
        CgpaService cgpaService = new CgpaService(subjectDao);

        // Create modern tab panels
        tabs.addTab("Student Management", new StudentFormPanel(studentDao));
        tabs.addTab("Subject Management", new SubjectFormPanel(studentDao, subjectDao));
        tabs.addTab("CGPA Calculator", new CgpaPanel(studentDao, cgpaService));
        
        add(tabs, BorderLayout.CENTER);
    }
    
    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }
} 