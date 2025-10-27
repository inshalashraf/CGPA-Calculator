package com.cgpa.frontend.ui.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class MenuButton extends JButton {

    private Color effectColor = new Color(173, 173, 173);
    private String icoName;

    public MenuButton() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(9, 10, 9, 10));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(new Color(0, 0, 0, 0)); // Start with transparent background
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (getEffectColor() != Color.WHITE) { // Only change background if not selected
                    setBackground(new Color(255, 255, 255, 30)); // Semi-transparent white
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(new Color(0, 0, 0, 0)); // Back to transparent
            }
        });
    }

    public String getIcoName() {
        return icoName;
    }

    public void setIcoName(String icoName) {
        this.icoName = icoName;
    }

    public Color getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(Color effectColor) {
        this.effectColor = effectColor;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getBackground() != null) {
            if (getBackground() instanceof Color) {
                g2.setColor(getBackground());
            } else {
                g2.setPaint(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }
        g2.dispose();
        super.paintComponent(grphcs);
    }
} 