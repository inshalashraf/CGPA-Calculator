package com.cgpa.frontend.ui.menu;

import com.cgpa.frontend.ui.swing.MenuButton;
import com.cgpa.frontend.ui.theme.SystemTheme;
import com.cgpa.frontend.ui.event.EventMenu;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

public class Menu extends javax.swing.JPanel {

    public int getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(int selectedLocation) {
        this.selectedLocation = selectedLocation;
        repaint();
    }

    public void addEventMenu(EventMenu event) {
        this.events.add(event);
    }

    private int selectedIndex = 0;
    private Animator animator;
    private TimingTarget target;
    private int selectedLocation = 250; // Adjusted for navigation tabs at the top to properly highlight Dashboard
    private int targetLocation;
    private List<EventMenu> events = new ArrayList<>();

    public Menu() {
        initComponents();
        setOpaque(false);
        setBackground(Color.WHITE);
        // Use MigLayout with centering - center items both horizontally and vertically
        menu.setLayout(new MigLayout("fillx, wrap, inset 0, aligny center, alignx center", "[fill]", "[fill, 36!]0[fill, 36!]0[fill, 36!]0[fill, 36!]"));
        initMenu();
    }

    private void initMenu() {
        addMenu("Dashboard", "🏠", 0);
        addMenu("Student Portal", "🎓", 1);
        addMenu("Faculty Portal", "👨‍🏫", 2);
        addMenu("Settings", "⚙️", 3);
        menu.repaint();
        menu.revalidate();
        setSelectedMenu(0);
        animator = new Animator(300);
        animator.addTarget(new TimingTargetAdapter() {
            @Override
            public void begin() {
                clearSelected();
            }

            @Override
            public void end() {
                setSelectedMenu(selectedIndex);
                runEvent();
            }
        });
        animator.setDeceleration(.5f);
        animator.setAcceleration(.5f);
        animator.setResolution(0);
    }

    private void addMenu(String menuName, String icon, int index) {
        MenuButton m = new MenuButton();
        m.setIcoName(icon);
        try {
            m.setIcon(new ImageIcon(getClass().getResource("/images/icons/" + icon + ".png")));
        } catch (Exception e) {
            // Use text instead of icon if image is not found
            m.setText(icon + "  " + menuName);
        }
        m.setFont(m.getFont().deriveFont(Font.BOLD, 12));
        m.setForeground(new Color(127, 127, 127));
        m.setHorizontalAlignment(JButton.LEFT);
        m.setText("  " + menuName);
        m.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (index != selectedIndex) {
                    if (animator.isRunning()) {
                        animator.stop();
                    }
                    int y = m.getY() + menu.getY();
                    targetLocation = y;
                    selectedIndex = index;
                    animator.removeTarget(target);
                    target = new PropertySetter(Menu.this, "selectedLocation", selectedLocation, targetLocation);
                    animator.addTarget(target);
                    animator.start();
                }
            }
        });
        menu.add(m);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int y = selectedLocation;
        g2.setColor(SystemTheme.mainColor);
        g2.fill(createShape(y));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    private Shape createShape(int y) {
        int width = getWidth() - 12;
        int r = 20;
        Area area = new Area(new RoundRectangle2D.Float(6, y, width, 35, r, r));
        area.add(new Area(new RoundRectangle2D.Float(width - r + 6, y, r, r, 5, 5)));
        area.add(new Area(new RoundRectangle2D.Float(6, y + 35 - r, r, r, 5, 5)));
        return area;
    }

    private void clearSelected() {
        for (Component com : menu.getComponents()) {
            if (com instanceof MenuButton) {
                MenuButton c = (MenuButton) com;
                c.setForeground(new Color(127, 127, 127));
                c.setEffectColor(new Color(173, 173, 173));
                c.setBackground(new Color(0, 0, 0, 0)); // Transparent background
                if (!c.getIcoName().contains("_s")) {
                    try {
                    c.setIcon(new ImageIcon(getClass().getResource("/images/icons/" + c.getIcoName() + ".png")));
                } catch (Exception e) {
                    // Keep current icon if normal icon is not found
                }
                }
            }
        }
    }

    public void setSelectedMenu(int index) {
        MenuButton cmd = (MenuButton) menu.getComponent(index);
        cmd.setForeground(Color.WHITE);
        cmd.setEffectColor(SystemTheme.mainColor); // Use the system theme color for consistency
        cmd.setBackground(new Color(0, 0, 0, 0)); // Keep transparent background
        try {
            cmd.setIcon(new ImageIcon(getClass().getResource("/images/icons/" + cmd.getIcoName() + "_s.png")));
        } catch (Exception e) {
            // Keep current icon if selected icon is not found
        }
    }

    private void runEvent() {
        for (EventMenu event : events) {
            event.selectedMenu(selectedIndex);
        }
    }

    private void initComponents() {
        menu = new javax.swing.JPanel();
        imageAvatar1 = new com.cgpa.frontend.ui.theme.ImageAvatar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        menu.setOpaque(false);

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE) // Back to 144px for 4 items (4 * 36px)
        );

        imageAvatar1.setBorderSize(2);
        imageAvatar1.setBorderSpace(1);
        imageAvatar1.setGradientColor1(new Color(18, 130, 227));
        imageAvatar1.setGradientColor2(new Color(226, 45, 60));
        imageAvatar1.setPreferredSize(new java.awt.Dimension(50, 50));
        imageAvatar1.setSize(50, 50);
        imageAvatar1.setMaximumSize(new java.awt.Dimension(50, 50));
        imageAvatar1.setMinimumSize(new java.awt.Dimension(50, 50));
        try {
            imageAvatar1.setImage(new ImageIcon(getClass().getResource("/images/logo.jpg")).getImage());
        } catch (Exception e) {
            // Create a simple colored circle if logo is not found
            imageAvatar1.setImage(null);
        }

        jLabel1.setFont(new Font("sansserif", Font.BOLD, 14)); // Reverted back to original size 14
        jLabel1.setForeground(new Color(117, 117, 117));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CGPA Calculator");

        jLabel2.setFont(new Font("sansserif", Font.BOLD, 12)); // Reverted back to original size 12
        jLabel2.setForeground(new Color(154, 154, 154));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Academic Excellence");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(imageAvatar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(menu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
                layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0) // Minimal top gap
                .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE) // Navigation tabs at the top
                .addGap(0, 0, 0) // Minimal gap
                .addComponent(imageAvatar1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.DEFAULT_SIZE) // Logo below navigation
                .addGap(0, 0, 0) // Minimal gap
                .addComponent(jLabel1) // CGPA Calculator title
                .addGap(0, 0, 0) // Minimal gap
                .addComponent(jLabel2) // Academic Excellence subtitle
                .addGap(10, 10, 10)) // Added bottom spacing for better visual balance
        );
    }

    private com.cgpa.frontend.ui.theme.ImageAvatar imageAvatar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel menu;
} 