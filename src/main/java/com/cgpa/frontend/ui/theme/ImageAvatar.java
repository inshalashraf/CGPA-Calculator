package com.cgpa.frontend.ui.theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageAvatar extends JPanel {

    private Image image;
    private int borderSize = 4;
    private int borderSpace = 3;
    private Color gradientColor1 = new Color(18, 130, 227);
    private Color gradientColor2 = new Color(226, 45, 60);

    public ImageAvatar() {
        setPreferredSize(new java.awt.Dimension(80, 80));
        setOpaque(false);
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        repaint();
    }

    public void setBorderSpace(int borderSpace) {
        this.borderSpace = borderSpace;
        repaint();
    }

    public void setGradientColor1(Color gradientColor1) {
        this.gradientColor1 = gradientColor1;
        repaint();
    }

    public void setGradientColor2(Color gradientColor2) {
        this.gradientColor2 = gradientColor2;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int x = (width - size) / 2;
        int y = (height - size) / 2;
        
        // Draw gradient border
        g2.setPaint(new java.awt.GradientPaint(0, 0, gradientColor1, width, height, gradientColor2));
        g2.fill(new Ellipse2D.Double(x, y, size, size));
        
        // Draw image
        if (image != null) {
            int imageSize = size - (borderSize + borderSpace) * 2;
            int imageX = x + borderSize + borderSpace;
            int imageY = y + borderSize + borderSpace;
            g2.setClip(new Ellipse2D.Double(imageX, imageY, imageSize, imageSize));
            g2.drawImage(image, imageX, imageY, imageSize, imageSize, null);
        }
        
        g2.dispose();
        super.paintComponent(grphcs);
    }
} 