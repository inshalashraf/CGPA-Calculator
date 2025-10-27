package com.cgpa.frontend.ui.theme;

import com.cgpa.frontend.ui.event.EventColorChange;
import com.cgpa.frontend.ui.swing.PanelBackground;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.jdesktop.animation.timing.Animator;

public class ThemeColorChange {

    public static ThemeColorChange instance;

    public static ThemeColorChange getInstance() {
        if (instance == null) {
            instance = new ThemeColorChange();
        }
        return instance;
    }

    private Mode mode;
    private Animator animator;
    private PanelBackground panelBackground;
    private List<ThemeColor> themeColor;
    private List<EventColorChange> eventsColorChange;

    private ThemeColorChange() {
        themeColor = new ArrayList<>();
        eventsColorChange = new ArrayList<>();
        animator = new Animator(300);
        animator.setResolution(5);
    }

    public void addThemes(ThemeColor theme) {
        themeColor.add(theme);
    }

    public void changeMode(Mode mode) {
        if (this.mode != mode) {
            this.mode = mode;
            for (ThemeColor color : themeColor) {
                animator.removeTarget(color.getTarget());
                animator.addTarget(color.newTarget(mode));
            }
            animator.start();
        }
    }

    public void addEventColorChange(EventColorChange event) {
        eventsColorChange.add(event);
    }

    public void runEventColorChange(Color color) {
        for (EventColorChange event : eventsColorChange) {
            event.colorChange(color);
        }
    }

    public void initBackground(PanelBackground panelBackground) {
        this.panelBackground = panelBackground;
    }

    public void changeBackgroundImage(String image) {
        if (image.equals("")) {
            panelBackground.setImage(null);
        } else {
            // Primary location: classpath resources at /background/<image>
            String primaryPath = "/background/" + image;
            java.net.URL location = getClass().getResource(primaryPath);
            if (location == null) {
                // Fallback: older layout under /images/backgrounds/<image>
                String fallbackPath = "/images/backgrounds/" + image;
                location = getClass().getResource(fallbackPath);
            }
            if (location != null) {
                panelBackground.setImage(new ImageIcon(location).getImage());
            } else {
                // If not found, clear image to avoid NPE
                panelBackground.setImage(null);
            }
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public static enum Mode {
        DARK, LIGHT
    }
} 