package com.cgpa.frontend.ui.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class SwitchButton extends JPanel {

    private boolean selected = false;
    private SwitchButtonEvent event;
    private boolean mouseOver = false;
    private Color themeColor = new Color(52, 152, 219); // Default blue color

    public SwitchButton() {
        setPreferredSize(new Dimension(40, 20)); // Smaller, more compact size
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false); // Make panel transparent
        init();
    }

    private void init() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                mouseOver = false;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (mouseOver) {
                    selected = !selected;
                    if (event != null) {
                        event.onSelected(selected);
                    }
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = ( Graphics2D ) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        
        // Make it more rounded - use height as radius for pill shape
        int r = height;
        
        if (selected) {
            // Selected state - filled with accent color (theme color)
            g2.setColor(themeColor); // Use theme color instead of hardcoded blue
            g2.fillRoundRect(0, 0, width, height, r, r);
            g2.setColor(Color.WHITE);
            // Smaller, more proportional circle with fixed positioning
            int circleSize = height - 4;
            // Fixed right position for perfect alignment
            int x = width - circleSize - 2;
            g2.fillOval(x, 2, circleSize, circleSize);
        } else {
            // Unselected state - grey background with white circle
            g2.setColor(new Color(120, 120, 120));
            g2.fillRoundRect(0, 0, width, height, r, r);
            g2.setColor(Color.WHITE);
            // Smaller, more proportional circle with fixed positioning
            int circleSize = height - 4;
            // Fixed left position for perfect alignment
            int x = 2;
            g2.fillOval(x, 2, circleSize, circleSize);
        }
        
        g2.dispose();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public void setThemeColor(Color color) {
        this.themeColor = color;
        repaint();
    }

    public void addEventSelected(SwitchButtonEvent event) {
        this.event = event;
    }

    // Return null for animator since we're not using animation
    public Object getAnimator() {
        return null;
    }

    public interface SwitchButtonEvent {
        void onSelected(boolean selected);
    }
} 