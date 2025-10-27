package com.cgpa.frontend.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernLandingPanel extends JPanel {
    private final Listener listener;
    
    // Enhanced colors for modern theme
    private static final Color CARD_BG = new Color(255, 255, 255, 250);
    private static final Color CARD_HOVER = new Color(255, 255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 40);
    private static final Color GRADIENT_START = new Color(255, 255, 255, 0);
    private static final Color GRADIENT_END = new Color(255, 255, 255, 50);

    public interface Listener {
        void onStudent();
        void onFaculty();
        void onToggleTheme();
    }

    public ModernLandingPanel(Listener listener) {
        this.listener = listener;
        setupPanel();
        createWelcomeSection();
        createPortalCards();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255, 0)); // Transparent
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);
    }

    private void createWelcomeSection() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome to GradeHub Management System");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(30, 30, 30));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Choose your portal to get started");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(80, 80, 80));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(subtitleLabel);

        add(welcomePanel, BorderLayout.NORTH);
    }

    private void createPortalCards() {
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridBagLayout());
        cardsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Student Portal Card
        JPanel studentCard = createSexyPortalCard(
            "🎓",
            "Student Portal",
            "Manage your academic records, subjects, and calculate your CGPA with beautiful visualizations",
            new Color(52, 152, 219),
            new Color(41, 128, 185),
            () -> listener.onStudent()
        );

        // Faculty Portal Card
        JPanel facultyCard = createSexyPortalCard(
            "👨‍🏫",
            "Faculty Portal",
            "Access student information and calculate CGPA for your students with detailed analytics",
            new Color(231, 76, 60),
            new Color(192, 57, 43),
            () -> listener.onFaculty()
        );

        // Theme Toggle Card
        JPanel themeCard = createSexyPortalCard(
            "🎨",
            "Theme Settings",
            "Switch between light and dark themes for a personalized experience",
            new Color(155, 89, 182),
            new Color(142, 68, 173),
            () -> listener.onToggleTheme()
        );

        // Layout cards in a responsive grid
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(15, 15, 15, 15);
        cardsPanel.add(studentCard, gbc);
        
        gbc.gridx = 1;
        cardsPanel.add(facultyCard, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        cardsPanel.add(themeCard, gbc);

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createSexyPortalCard(String emoji, String title, String description, Color primaryColor, Color secondaryColor, Runnable action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create rounded rectangle
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, primaryColor,
                    getWidth(), getHeight(), secondaryColor
                );
                g2d.setPaint(gradient);
                g2d.fill(roundedRectangle);
                
                // Add subtle inner glow
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(roundedRectangle);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        card.setLayout(new BorderLayout(20, 20));
        card.setOpaque(false);
                        card.setPreferredSize(new Dimension(380, 250));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Create content panel with glass effect
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create glass effect background
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Semi-transparent white with blur effect
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fill(roundedRectangle);
                
                // Add subtle border
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(roundedRectangle);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        contentPanel.setLayout(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with emoji and title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        headerPanel.setOpaque(false);
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 40, 40));
        
        headerPanel.add(emojiLabel);
        headerPanel.add(titleLabel);

        // Description with better typography
        JTextArea descArea = new JTextArea(description);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setForeground(new Color(60, 60, 60));
        descArea.setRows(4);
        descArea.setBackground(new Color(255, 255, 255, 0));
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        // Add components to content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(descArea, BorderLayout.CENTER);

        // Add content panel to card
        card.add(contentPanel, BorderLayout.CENTER);

        // Enhanced hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Scale up effect
                card.setPreferredSize(new Dimension(390, 255));
                card.revalidate();
                
                // Add shadow effect
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Scale back to normal
                card.setPreferredSize(new Dimension(380, 250));
                card.revalidate();
                
                // Remove shadow
                card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Add click animation
                card.setPreferredSize(new Dimension(375, 245));
                card.revalidate();
                
                // Reset after animation
                Timer timer = new Timer(150, evt -> {
                    card.setPreferredSize(new Dimension(380, 250));
                    card.revalidate();
                    action.run();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        return card;
    }
} 