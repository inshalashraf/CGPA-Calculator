package com.cgpa.frontend.ui.form;

import com.cgpa.frontend.ui.component.Form;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.geom.RoundRectangle2D;

public class HomeForm extends Form {

    public HomeForm() {
        initComponents();
    }

    @Override
    public void changeColor(Color color) {
        // Update colors when theme changes
    }
    


    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 80)); // Semi-transparent black instead of fully transparent
        
        // Welcome Section
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        welcomePanel.setBackground(new Color(0, 0, 0, 80)); // Semi-transparent black instead of fully transparent
        welcomePanel.setLayout(new javax.swing.BoxLayout(welcomePanel, javax.swing.BoxLayout.Y_AXIS));
        welcomePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel welcomeLabel = new JLabel("Welcome to GradeHub Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Academic Excellence Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(subtitleLabel);
        
        // Stats Section
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(0, 0, 0, 80)); // Semi-transparent black instead of fully transparent
        statsPanel.setLayout(new javax.swing.BoxLayout(statsPanel, javax.swing.BoxLayout.X_AXIS));
        statsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Add some placeholder stats
        statsPanel.add(createStatCard("Total Students", "0", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Subjects", "0", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Average CGPA", "0.00", new Color(155, 89, 182)));
        
        add(welcomePanel);
        add(statsPanel);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient effect
                g2.setPaint(new java.awt.GradientPaint(0, 0, color, getWidth(), getHeight(), color.darker()));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                
                // Add subtle border
                g2.setColor(color.brighter());
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 15, 15));
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setBackground(new Color(0, 0, 0, 80)); // Semi-transparent black instead of fully transparent
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setMaximumSize(new java.awt.Dimension(250, 180));
        card.setPreferredSize(new java.awt.Dimension(250, 180));
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
        });
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valueLabel);
        card.add(titleLabel);
        
        return card;
    }
} 