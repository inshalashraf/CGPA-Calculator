package com.cgpa.frontend.ui.shell;

import com.cgpa.backend.service.FacultyAuthService;
import com.cgpa.frontend.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernDashboardFrame extends JFrame {
    private JPanel sidebar;
    private JPanel contentPanel;
    private JPanel currentContent;
    private ModernLandingPanel landingPanel;
    private FacultyAuthService authService;
    private boolean sidebarVisible = true;
    private JButton toggleSidebarButton;
    
    // Colors for NEON theme - attractive and appealing!
    private static final Color SIDEBAR_BG = new Color(15, 15, 25); // Deep dark blue-black
    private static final Color SIDEBAR_HOVER = new Color(25, 25, 40); // Slightly lighter on hover
    private static final Color CONTENT_BG = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0, 255, 255); // Bright cyan/neon blue
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color NEON_PINK = new Color(255, 20, 147); // Hot pink
    private static final Color NEON_GREEN = new Color(57, 255, 20); // Bright green
    private static final Color NEON_PURPLE = new Color(138, 43, 226); // Blue violet
    private static final Color NEON_ORANGE = new Color(255, 69, 0); // Red orange
    private static final Color NEON_YELLOW = new Color(255, 255, 0); // Bright yellow
    private static final Color NEON_CYAN = new Color(0, 255, 255); // Cyan

    public ModernDashboardFrame() {
        setupFrame();
        createSidebar();
        createContentArea();
        authService = new FacultyAuthService();
        showLandingPage();
    }

    private void setupFrame() {
        setTitle("CGPA Calculator - Modern Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Apply light theme by default for better aesthetics
        ThemeManager.initLight();
        
        // Set beautiful background
        setContentPane(createBackgroundPanel());
    }
    
    private JPanel createBackgroundPanel() {
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Create NEON gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(10, 10, 20),
                    getWidth(), getHeight(), new Color(25, 25, 45)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add NEON pattern overlay with glowing dots
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 20));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2d.fillOval(i, j, 4, 4);
                    }
                }
                
                // Add colored NEON dots for variety
                g2d.setColor(new Color(NEON_PINK.getRed(), NEON_PINK.getGreen(), NEON_PINK.getBlue(), 15));
                for (int i = 20; i < getWidth(); i += 80) {
                    for (int j = 20; j < getHeight(); j += 80) {
                        g2d.fillOval(i, j, 3, 3);
                    }
                }
                
                // Add subtle NEON grid lines
                g2d.setColor(new Color(NEON_PURPLE.getRed(), NEON_PURPLE.getGreen(), NEON_PURPLE.getBlue(), 12));
                for (int i = 0; i < getWidth(); i += 60) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int j = 0; j < getHeight(); j += 60) {
                    g2d.drawLine(0, j, getWidth(), j);
                }
                
                g2d.dispose();
            }
        };
        backgroundPanel.setOpaque(false);
        return backgroundPanel;
    }
    

    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sidebar.setName("sidebar"); // For identification

        // Enhanced Header Section with NEON gradient background
        JPanel headerSection = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create NEON gradient background for header
                GradientPaint gradient = new GradientPaint(
                    0, 0, NEON_PURPLE,
                    getWidth(), getHeight(), NEON_PINK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add NEON inner glow with multiple colors
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 60));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
                
                // Add subtle NEON accent lines
                g2d.setColor(NEON_GREEN);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(10, getHeight() - 5, getWidth() - 10, getHeight() - 5);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        headerSection.setPreferredSize(new Dimension(200, 100));
        headerSection.setMaximumSize(new Dimension(200, 100));
        headerSection.setOpaque(false);
        headerSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo/Title with enhanced styling - split into two lines
        JLabel titleLabel1 = new JLabel("CGPA");
        titleLabel1.setForeground(Color.WHITE);
        titleLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel1.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel titleLabel2 = new JLabel("CALCULATOR");
        titleLabel2.setForeground(Color.WHITE);
        titleLabel2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel2.setBorder(BorderFactory.createEmptyBorder(2, 0, 10, 0));
        
        // Add subtitle for extra appeal
        JLabel subtitleLabel = new JLabel("Academic Excellence");
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        headerSection.add(titleLabel1);
        headerSection.add(titleLabel2);
        headerSection.add(subtitleLabel);
        sidebar.add(headerSection);
        
        // Add NEON separator line
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create NEON gradient separator line
                GradientPaint lineGradient = new GradientPaint(
                    20, 0, ACCENT_COLOR,
                    180, 0, NEON_GREEN
                );
                g2d.setPaint(lineGradient);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(20, 0, 180, 0);
                
                // Add glow effect
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 40));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(20, 0, 180, 0);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        separator.setPreferredSize(new Dimension(200, 2));
        separator.setMaximumSize(new Dimension(200, 2));
        separator.setOpaque(false);
        sidebar.add(separator);

        // Add flexible space to push navigation items to center
        sidebar.add(Box.createVerticalGlue());

        // Navigation Items
        addNavItem("🏠", "HOME", this::showLandingPage);
        addNavItem("🎓", "STUDENT", this::showStudentDashboard);
        addNavItem("👨‍🏫", "FACULTY", this::showFacultyDashboard);
        
        // Theme Toggle
        addNavItem("🎨", "THEME", this::toggleTheme);
        
        // Exit
        addNavItem("❌", "EXIT", () -> System.exit(0));

        // Add flexible space after navigation items
        sidebar.add(Box.createVerticalGlue());
        
        // Add footer section with NEON styling
        JPanel footerSection = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create NEON footer background
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 20));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add NEON top border
                g2d.setColor(NEON_ORANGE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(20, 0, 180, 0);
                
                // Add subtle NEON glow
                g2d.setColor(new Color(NEON_ORANGE.getRed(), NEON_ORANGE.getGreen(), NEON_ORANGE.getBlue(), 40));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(20, 0, 180, 0);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        footerSection.setLayout(new BoxLayout(footerSection, BoxLayout.Y_AXIS));
        footerSection.setPreferredSize(new Dimension(200, 60));
        footerSection.setMaximumSize(new Dimension(200, 60));
        footerSection.setOpaque(false);
        footerSection.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel versionLabel = new JLabel("v2.0");
        versionLabel.setForeground(NEON_GREEN);
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel poweredByLabel = new JLabel("Powered by Java Swing");
        poweredByLabel.setForeground(NEON_ORANGE);
        poweredByLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        poweredByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        poweredByLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        
        footerSection.add(versionLabel);
        footerSection.add(poweredByLabel);
        sidebar.add(footerSection);

        add(sidebar, BorderLayout.WEST);
    }

    private void addNavItem(String emoji, String text, Runnable action) {
        JPanel navItem = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create rounded rectangle background with subtle NEON glow
                int arc = 15;
                g2d.setColor(new Color(25, 25, 40)); // Better contrast with sidebar
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                
                // Add subtle NEON border
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 40));
                g2d.setStroke(new BasicStroke(1)); // Thinner border
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);
                
                // Add very subtle inner glow
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 15));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, arc, arc);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        navItem.setLayout(new BoxLayout(navItem, BoxLayout.X_AXIS));
        navItem.setOpaque(false);
        navItem.setMaximumSize(new Dimension(180, 50));
        navItem.setPreferredSize(new Dimension(180, 50));
        navItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navItem.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        navItem.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Emoji label
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        emojiLabel.setForeground(Color.WHITE);
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emojiLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        emojiLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        // Text label
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        textLabel.setForeground(Color.WHITE);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        navItem.add(emojiLabel);
        navItem.add(textLabel);

        // Enhanced hover effects with smooth transitions
        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Create a timer for smooth hover effect
                Timer hoverTimer = new Timer(50, evt -> {
                    // Dark neon background change - better color combination
                    navItem.setBackground(new Color(25, 35, 55, 240)); // Dark blue-gray
                    emojiLabel.setForeground(ACCENT_COLOR);
                    emojiLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
                    textLabel.setForeground(ACCENT_COLOR);
                    textLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    navItem.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_COLOR, 1), // Much thinner border
                        BorderFactory.createEmptyBorder(7, 9, 7, 9)
                    ));
                });
                hoverTimer.setRepeats(false);
                hoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Create a timer for smooth exit effect
                Timer exitTimer = new Timer(50, evt -> {
                    navItem.setBackground(SIDEBAR_BG);
                    emojiLabel.setForeground(Color.WHITE);
                    emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                    textLabel.setForeground(Color.WHITE);
                    textLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    navItem.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                });
                exitTimer.setRepeats(false);
                exitTimer.start();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Add click animation
                Timer clickTimer = new Timer(100, evt -> {
                    action.run();
                });
                clickTimer.setRepeats(false);
                clickTimer.start();
            }
        });
        
        // Also make both labels clickable with same effects
        emojiLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.dispatchEvent(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navItem.dispatchEvent(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navItem.dispatchEvent(e);
            }
        });
        
        textLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                navItem.dispatchEvent(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                navItem.dispatchEvent(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navItem.dispatchEvent(e);
            }
        });

        sidebar.add(navItem);
        sidebar.add(Box.createVerticalStrut(2));
    }

    private void createContentArea() {
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create NEON content background
                g2d.setColor(new Color(20, 20, 35, 220)); // Dark with transparency
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add subtle NEON border
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), 40));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);
    }

    private void showLandingPage() {
        if (landingPanel == null) {
            landingPanel = new ModernLandingPanel(new ModernLandingPanel.Listener() {
                @Override
                public void onStudent() {
                    showStudentDashboard();
                }

                @Override
                public void onFaculty() {
                    showFacultyDashboard();
                }

                @Override
                public void onToggleTheme() {
                    toggleTheme();
                }
            });
        }
        setContent(landingPanel);
    }

    private void showStudentDashboard() {
        ModernStudentDashboard studentDashboard = new ModernStudentDashboard();
        studentDashboard.setParentFrame(this);
        setContent(studentDashboard);
    }

    private void showFacultyDashboard() {
        LoginDialog dialog = new LoginDialog(this, authService);
        dialog.setVisible(true);
        if (dialog.isAuthenticated()) {
            ModernFacultyDashboard facultyDashboard = new ModernFacultyDashboard();
            facultyDashboard.setParentFrame(this);
            setContent(facultyDashboard);
        }
    }

    private void setContent(JPanel content) {
        if (currentContent != null) {
            contentPanel.remove(currentContent);
        }
        
        currentContent = content;
        contentPanel.add(currentContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void toggleTheme() {
        ThemeManager.toggle();
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ModernDashboardFrame().setVisible(true);
        });
    }
} 