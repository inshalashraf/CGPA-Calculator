package com.cgpa.frontend.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;

public class FacultyFlashcard extends JPanel {
    
    private String title;
    private String description;
    private String icon;
    private Color backgroundColor;
    private boolean isHovered = false;
    private ActionListener clickListener;
    
    // Animation properties
    private float hoverProgress = 0.0f;
    private Timer animationTimer;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    
    public FacultyFlashcard(String title, String description, String icon, Color backgroundColor) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.backgroundColor = backgroundColor;
        
        setPreferredSize(new Dimension(300, 220));
        setMaximumSize(new Dimension(300, 220));
        setMinimumSize(new Dimension(300, 220));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        setupMouseListeners();
        setupAnimationTimer();
    }
    
    private void setupAnimationTimer() {
        animationTimer = new Timer(16, e -> {
            if (isHovered && hoverProgress < 1.0f) {
                hoverProgress = Math.min(1.0f, hoverProgress + 0.1f);
            } else if (!isHovered && hoverProgress > 0.0f) {
                hoverProgress = Math.max(0.0f, hoverProgress - 0.1f);
            }
            
            if (hoverProgress == 0.0f || hoverProgress == 1.0f) {
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
    }
    
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.actionPerformed(null);
                }
            }
        });
    }
    
    public void setClickListener(ActionListener listener) {
        this.clickListener = listener;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enhanced rendering hints for perfect anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        int width = getWidth();
        int height = getHeight();
        
        // Calculate hover effects
        float scale = 1.0f + (hoverProgress * 0.05f); // 5% scale increase
        float elevation = hoverProgress * 12.0f; // 12px elevation for more visible effect
        
        // Create transform for bulging effect - apply to ENTIRE card
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(width / 2, height / 2);
        g2d.scale(scale, scale);
        g2d.translate(-width / 2, -height / 2);
        
        // Draw glow effect with better visibility
        drawGlowEffect(g2d, width, height, elevation);
        
        // Draw main card with glassmorphism
        drawGlassmorphicCard(g2d, width, height, elevation);
        
        // Draw content
        drawContent(g2d, width, height);
        
        // Restore transform
        g2d.setTransform(originalTransform);
        g2d.dispose();
    }
    
    private void drawGlowEffect(Graphics2D g2d, int width, int height, float elevation) {
        // Create a single, clean glow layer that doesn't interfere with corners
        float glowIntensity = 1.0f + hoverProgress * 0.3f; // Subtle glow increase on hover
        
        RadialGradientPaint glowPaint = new RadialGradientPaint(
            width / 2, height / 2, Math.max(width, height) / 2 + 25,
            new float[]{0.0f, 0.8f, 1.0f},
            new Color[]{
                new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), (int)(60 * glowIntensity)),
                new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), (int)(30 * glowIntensity)),
                new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 0)
            }
        );
        
        g2d.setPaint(glowPaint);
        g2d.fillOval(-25, -25, width + 50, height + 50);
    }
    
    private void drawGlassmorphicCard(Graphics2D g2d, int width, int height, float elevation) {
        int arc = 35; // Increased arc for even smoother corners
        
        // Create a single, clean rounded rectangle with perfect anti-aliasing
        RoundRectangle2D mainRect = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        
        // 1. Main background with transparency - single layer
        Color mainColor = new Color(
            backgroundColor.getRed(), 
            backgroundColor.getGreen(), 
            backgroundColor.getBlue(), 
            230 // Slightly more opaque for better visibility
        );
        g2d.setColor(mainColor);
        g2d.fill(mainRect);
        
        // 2. Top highlight (glass effect) - use clipping to avoid corner issues
        g2d.setClip(mainRect);
        LinearGradientPaint highlightPaint = new LinearGradientPaint(
            0, 0, 0, height / 3,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{
                new Color(255, 255, 255, 60),
                new Color(255, 255, 255, 20),
                new Color(255, 255, 255, 0)
            }
        );
        g2d.setPaint(highlightPaint);
        g2d.fillRect(0, 0, width, height / 3);
        g2d.setClip(null); // Reset clip
        
        // 3. Clean border with perfect anti-aliasing
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(mainRect);
        
        // 4. Subtle inner glow effect (no more inner shadow that causes jagged edges)
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(new RoundRectangle2D.Float(1, 1, width - 2, height - 2, arc - 1, arc - 1));
    }
    
    private void drawContent(Graphics2D g2d, int width, int height) {
        // Draw icon with modern styling
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int iconX = (width - fm.stringWidth(icon)) / 2;
        int iconY = 70;
        
        // Add subtle shadow to icon
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.drawString(icon, iconX + 2, iconY + 2);
        g2d.setColor(Color.WHITE);
        g2d.drawString(icon, iconX, iconY);
        
        // Draw title with modern typography
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2d.setColor(Color.WHITE);
        fm = g2d.getFontMetrics();
        int titleX = (width - fm.stringWidth(title)) / 2;
        int titleY = 110;
        g2d.drawString(title, titleX, titleY);
        
        // Draw description with bullet points
        drawBulletDescription(g2d, width, height);
    }
    
    private void drawBulletDescription(Graphics2D g2d, int width, int height) {
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2d.setColor(new Color(240, 240, 240));
        
        // Split description into bullet points
        String[] sentences = description.split("\\.");
        int startY = 140;
        int lineHeight = 18;
        int bulletSize = 4;
        
        for (int i = 0; i < sentences.length && i < 3; i++) {
            String sentence = sentences[i].trim();
            if (sentence.isEmpty()) continue;
            
            int y = startY + (i * lineHeight);
            
            // Draw bullet point with smooth edges
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.fillOval(30, y - bulletSize, bulletSize, bulletSize);
            
            // Draw text
            g2d.setColor(new Color(240, 240, 240));
            g2d.drawString(sentence, 45, y);
        }
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
} 