package com.cgpa.frontend.ui;

import com.cgpa.backend.service.FacultyAuthService;
import com.cgpa.frontend.ui.component.Header;
import com.cgpa.frontend.ui.component.MainBody;
import com.cgpa.frontend.ui.event.EventColorChange;
import com.cgpa.frontend.ui.event.EventMenu;
import com.cgpa.frontend.ui.form.HomeForm;
import com.cgpa.frontend.ui.form.SettingForm;
import com.cgpa.frontend.ui.form.StudentPortalForm;
import com.cgpa.frontend.ui.form.FacultyPortalForm;
import com.cgpa.frontend.ui.menu.Menu;
import com.cgpa.frontend.ui.swing.PanelBackground;
import com.cgpa.frontend.ui.theme.SystemTheme;
import com.cgpa.frontend.ui.theme.ThemeColor;
import com.cgpa.frontend.ui.theme.ThemeColorChange;
import java.awt.Color;
import javax.swing.JFrame;

public class DashboardFrame extends JFrame {

    private SettingForm settingForm;
    private StudentPortalForm studentPortalForm;
    private FacultyPortalForm facultyPortalForm;
    private FacultyAuthService authService;

    public DashboardFrame() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        init();
    }

    private void init() {
        header.initMoving(this);
        header.initEvent(panelBackground1);
        menu.addEventMenu(new EventMenu() {
            @Override
            public void selectedMenu(int index) {
                if (index == 0) {
                    mainBody.displayForm(new HomeForm());
                } else if (index == 1) {
                    showStudentDashboard();
                } else if (index == 2) {
                    showFacultyDashboard();
                } else if (index == 3) {
                    mainBody.displayForm(settingForm, "Settings");
                }
            }
        });
        
        // Initialize theme system
        ThemeColorChange.getInstance().addThemes(new ThemeColor(new Color(34, 34, 34), Color.WHITE) {
            @Override
            public void onColorChange(Color color) {
                panelBackground1.setBackground(color);
            }
        });
        
        ThemeColorChange.getInstance().addThemes(new ThemeColor(Color.WHITE, new Color(80, 80, 80)) {
            @Override
            public void onColorChange(Color color) {
                mainBody.changeColor(color);
            }
        });
        
        ThemeColorChange.getInstance().initBackground(panelBackground1);
        
        // Set default dark mode and background
        ThemeColorChange.getInstance().setMode(ThemeColorChange.Mode.DARK);
        panelBackground1.setBackground(new Color(34, 34, 34));
        mainBody.changeColor(Color.WHITE);
        
        // Set default background image and ensure it's enabled
        ThemeColorChange.getInstance().changeBackgroundImage("bg_2.jpg");
        
        SystemTheme.mainColor = new Color(18, 130, 227);
        settingForm = new SettingForm();
        settingForm.setEventColorChange(new EventColorChange() {
            @Override
            public void colorChange(Color color) {
                SystemTheme.mainColor = color;
                ThemeColorChange.getInstance().runEventColorChange(color);
                repaint();
            }
        });
        settingForm.setSelectedThemeColor(SystemTheme.mainColor);
        settingForm.setDarkMode(true);
        // Initialize with background image enabled and image 2 selected
        settingForm.initBackgroundImage("bg_2.jpg");
        mainBody.displayForm(new HomeForm());
    }

    private void showStudentDashboard() {
        if (studentPortalForm == null) {
            studentPortalForm = new StudentPortalForm();
        }
        mainBody.displayForm(studentPortalForm);
    }

    private void showFacultyDashboard() {
        if (facultyPortalForm == null) {
            facultyPortalForm = new FacultyPortalForm();
        }
        mainBody.displayForm(facultyPortalForm);
    }

    private void initComponents() {
        panelBackground1 = new PanelBackground();
        header = new Header();
        menu = new Menu();
        mainBody = new MainBody();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelBackground1.setBackground(new Color(34, 34, 34));

        javax.swing.GroupLayout panelBackground1Layout = new javax.swing.GroupLayout(panelBackground1);
        panelBackground1.setLayout(panelBackground1Layout);
        panelBackground1Layout.setHorizontalGroup(
            panelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelBackground1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainBody, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBackground1Layout.setVerticalGroup(
            panelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackground1Layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBackground1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                    .addComponent(mainBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackground1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
        setSize(1200, 800);
    }

    private Header header;
    private MainBody mainBody;
    private Menu menu;
    private PanelBackground panelBackground1;
} 