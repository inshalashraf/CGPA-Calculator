package com.cgpa.frontend.ui.component;

import com.cgpa.frontend.ui.event.EventColorChange;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class ColorOption extends JPanel {

    private EventColorChange event;
    private Color selectedColor = new Color(18, 130, 227);
    private Color[] colors = {
        new Color(18, 130, 227),
        new Color(226, 45, 60),
        new Color(46, 125, 50),
        new Color(255, 152, 0),
        new Color(156, 39, 176),
        new Color(0, 150, 136)
    };

    public ColorOption() {
        setPreferredSize(new Dimension(200, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false); // Make panel transparent
        init();
    }

    private void init() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();
                int index = getColorIndex(x, y);
                if (index >= 0) {
                    selectedColor = colors[index];
                    if (event != null) {
                        event.colorChange(selectedColor);
                    }
                    repaint();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent me) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    private int getColorIndex(int x, int y) {
        int itemWidth = getWidth() / colors.length;
        int index = x / itemWidth;
        if (index >= 0 && index < colors.length && y >= 0 && y <= getHeight()) {
            return index;
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int itemWidth = getWidth() / colors.length;
        int padding = 4;
        int itemHeight = getHeight() - (padding * 2);
        
        for (int i = 0; i < colors.length; i++) {
            int x = i * itemWidth + padding;
            int y = padding;
            
            // Draw color circle
            g2.setColor(colors[i]);
            g2.fillOval(x, y, itemHeight, itemHeight);
            
            // Draw selection indicator
            if (colors[i].equals(selectedColor)) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(3));
                g2.drawOval(x - 2, y - 2, itemHeight + 4, itemHeight + 4);
                
                // Add inner white dot for selected color
                g2.setColor(Color.WHITE);
                g2.fillOval(x + 3, y + 3, itemHeight - 6, itemHeight - 6);
            }
        }
        
        g2.dispose();
        super.paintComponent(grphcs);
    }

    public void setEvent(EventColorChange event) {
        this.event = event;
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
        repaint();
    }
} 