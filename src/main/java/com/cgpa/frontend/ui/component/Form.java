package com.cgpa.frontend.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JComponent;

public abstract class Form extends javax.swing.JPanel {
    
    private boolean isActive = false;
    
    public Form() {
        setOpaque(false);
    }
    
    public abstract void changeColor(Color color);
    
    public void setActive(boolean active) {
        this.isActive = active;
        setVisible(active);
        
        // Recursively set all child components
        for (Component child : getComponents()) {
            setComponentTreeVisible(child, active);
        }
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        // Only set children visible if this form is active
        if (visible && isActive) {
            for (Component child : getComponents()) {
                setComponentTreeVisible(child, visible);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // Don't paint anything if this form is not active
        if (!isActive) {
            return;
        }
        super.paintComponent(g);
    }
    
    private void setComponentTreeVisible(Component component, boolean visible) {
        if (component != null) {
            component.setVisible(visible);
            
            // If it's a container, recursively process its children
            if (component instanceof JComponent) {
                JComponent jcomp = (JComponent) component;
                for (Component child : jcomp.getComponents()) {
                    setComponentTreeVisible(child, visible);
                }
            }
        }
    }
} 