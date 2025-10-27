package com.cgpa.frontend.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.cgpa.frontend.ui.swing.PanelBackground;

public class Header extends javax.swing.JPanel {

    private JFrame frame;
    private PanelBackground panelBackground;
    private int x;
    private int y;

    public Header() {
        initComponents();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        init();
    }

    private void init() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                if (frame != null) {
                    frame.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
                }
            }
        });
    }

    public void initMoving(JFrame frame) {
        this.frame = frame;
    }

    public void initEvent(PanelBackground panelBackground) {
        this.panelBackground = panelBackground;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(grphcs);
    }

    private void initComponents() {
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
        
        // Add title label
        JLabel titleLabel = new JLabel("GradeHub");
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 0));
        
        // Add window control buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.X_AXIS));
        
        JButton minimizeBtn = createControlButton("−", new Color(255, 191, 0));
        JButton closeBtn = createControlButton("×", new Color(231, 76, 60));
        
        minimizeBtn.addActionListener(e -> {
            if (frame != null) {
                frame.setState(JFrame.ICONIFIED);
            }
        });
        
        closeBtn.addActionListener(e -> {
            if (frame != null) {
                frame.dispose();
            }
        });
        
        controlPanel.add(minimizeBtn);
        controlPanel.add(closeBtn);
        
        add(titleLabel);
        add(javax.swing.Box.createHorizontalGlue());
        add(controlPanel);
    }
    
    private JButton createControlButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new java.awt.Dimension(30, 30));
        button.setMaximumSize(new java.awt.Dimension(30, 30));
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
} 