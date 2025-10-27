package com.cgpa.frontend.ui;

import javax.swing.*;
import java.awt.Window;

public final class ThemeManager {
    private static boolean dark = false;

    private ThemeManager() {}

    public static void initLight() {
        dark = false;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }
        refreshUI();
    }

    public static void initDark() {
        dark = true;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }
        refreshUI();
    }

    public static void toggle() {
        if (dark) {
            initLight();
        } else {
            initDark();
        }
    }

    private static void refreshUI() {
        for (Window w : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(w);
        }
    }
}

