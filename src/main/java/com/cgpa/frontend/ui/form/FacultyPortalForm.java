package com.cgpa.frontend.ui.form;

import com.cgpa.backend.service.FacultyAuthService;
import com.cgpa.backend.service.CgpaService;
import com.cgpa.frontend.ui.component.Form;
import com.cgpa.frontend.ui.component.FacultyFlashcard;
import com.cgpa.frontend.ui.dialog.FacultyLoginDialog;
import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.model.Student;
import com.cgpa.backend.model.Subject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.awt.geom.Arc2D;

public class FacultyPortalForm extends Form {
    
    private FacultyAuthService authService;
    private boolean isAuthenticated = false;
    private String currentUsername = "";
    
    // UI Components
    private JPanel loginPanel;
    private JPanel dashboardPanel;
    private JPanel flashcardPanel;
    private JButton logoutButton;
    
    // Flashcards
    private FacultyFlashcard studentRecordsCard;
    private FacultyFlashcard subjectRecordsCard;
    private FacultyFlashcard resultRecordsCard;
    
    // Database and Table
    private StudentDao studentDao;
    private SubjectDao subjectDao;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    private DefaultTableModel subjectTableModel;
    private JTable subjectTable;
    
    // Subject Records state
    private boolean isFilteredByStudent = false;
    private String currentFilteredStudentRoll = "";
    private JButton backToStudentsButton;
    private Integer currentSemesterFilter = null;
    
    public FacultyPortalForm() {
        initServices();
        initComponents();
    }
    
    private void initServices() {
        try {
            authService = new FacultyAuthService();
            studentDao = new StudentDao();
            subjectDao = new SubjectDao();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing services: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void changeColor(Color color) {
        // Update colors when theme changes
    }
    
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            // Check authentication when form becomes active
            if (!isAuthenticated) {
                showLoginPanel();
            }
            repaint();
            revalidate();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); // Make transparent to show background image
        
        // Create login panel
        createLoginPanel();
        
        // Create dashboard panel
        createDashboardPanel();
        
        // Initially show login panel
        showLoginPanel();
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setOpaque(false);
        
        // Main content panel with semi-transparent black background
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(new Color(0, 0, 0, 120)); // Semi-transparent black background
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Faculty Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please login to access faculty features", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        
        // Login form panel
        JPanel loginFormPanel = createEmbeddedLoginForm();
        
        // Center panel for title, subtitle, and login form
        JPanel centerPanel = new JPanel(new BorderLayout(30, 0));
        centerPanel.setOpaque(false);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        centerPanel.add(textPanel, BorderLayout.NORTH);
        centerPanel.add(loginFormPanel, BorderLayout.CENTER);
        
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);
        loginPanel.add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createEmbeddedLoginForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);
        
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 35));
        
        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 35));
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(loginButton, gbc);
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin(usernameField.getText(), passwordField.getPassword());
            }
        });
        
        // Enter key in password field
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    performLogin(usernameField.getText(), passwordField.getPassword());
                }
            }
        });
        
        // Enter key in username field
        usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        
        return formPanel;
    }
    
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setOpaque(false);
        
        // Main content panel with semi-transparent black background
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(0, 0, 0, 120)); // Semi-transparent black background
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, Faculty Member!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogoutConfirmation();
            }
        });
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Create flashcard panel
        createFlashcardPanel();
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(flashcardPanel, BorderLayout.CENTER);
        
        dashboardPanel.add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private void createFlashcardPanel() {
        flashcardPanel = new JPanel(new BorderLayout());
        flashcardPanel.setOpaque(false);
        flashcardPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Top row with 2 cards
        // Dashboard Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel dashboardTitle = new JLabel("Faculty Dashboard");
        dashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        dashboardTitle.setForeground(Color.WHITE);
        titlePanel.add(dashboardTitle);
        
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        topRow.setOpaque(false);
        
        // Student Records Card
        studentRecordsCard = new FacultyFlashcard(
            "Student Records", 
            "View and manage student information. Personal details and academic records. Complete student management system.", 
            "👥", 
            new Color(52, 152, 219)
        );
        studentRecordsCard.setClickListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentRecords();
            }
        });
        
        // Subject Records Card
        subjectRecordsCard = new FacultyFlashcard(
            "Subject Records", 
            "Manage course subjects and curriculum details. Academic programs and course structure. Complete subject management.", 
            "📚", 
            new Color(46, 204, 113)
        );
        subjectRecordsCard.setClickListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSubjectRecords();
            }
        });
        
        topRow.add(studentRecordsCard);
        topRow.add(subjectRecordsCard);
        
        // Bottom row with 1 centered card - with proper spacing
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // Add top margin for spacing
        
        // Result Records Card
        resultRecordsCard = new FacultyFlashcard(
            "Result Records", 
            "Access and manage student grades. CGPA calculations and academic performance. Complete result management system.", 
            "📊", 
            new Color(155, 89, 182)
        );
        resultRecordsCard.setClickListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showResultRecords();
            }
        });
        
        bottomRow.add(resultRecordsCard);
        
        // Add rows to flashcard panel with increased spacing
        JPanel centerPanel = new JPanel(new BorderLayout(50, 20)); // Increased spacing between rows
        centerPanel.setOpaque(false);
        
        // Create a panel for title and top row
        JPanel topSection = new JPanel(new BorderLayout(0, 30));
        topSection.setOpaque(false);
        topSection.add(titlePanel, BorderLayout.NORTH);
        topSection.add(topRow, BorderLayout.CENTER);
        
        centerPanel.add(topSection, BorderLayout.NORTH);
        centerPanel.add(bottomRow, BorderLayout.CENTER);
        
        flashcardPanel.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void showLoginPanel() {
        removeAll();
        add(loginPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void showDashboardPanel() {
        removeAll();
        add(dashboardPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void performLogin(String username, char[] password) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password == null || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter a password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authenticate
        if (authService.authenticate(username.trim(), password)) {
            isAuthenticated = true;
            currentUsername = username.trim();
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + currentUsername + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            showDashboardPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Clear password for security
        java.util.Arrays.fill(password, '0');
    }
    
    private void showLogoutConfirmation() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            logout();
        }
    }
    
    private void logout() {
        isAuthenticated = false;
        currentUsername = "";
        showLoginPanel();
    }
    
    // Flashcard action methods (to be implemented later)
    private void showStudentRecords() {
        // Create a new panel for student records with proper black background
        JPanel studentRecordsPanel = new JPanel(new BorderLayout());
        studentRecordsPanel.setOpaque(true); // Make it opaque to show background
        studentRecordsPanel.setBackground(new Color(0, 0, 0, 180)); // More opaque black background

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(true); // Make header opaque
        headerPanel.setBackground(new Color(0, 0, 0, 200)); // Darker header background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a panel for the title and subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        
        JLabel subtitleLabel = new JLabel("Faculty Dashboard", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(135, 206, 235)); // Light blue color
        
        JLabel titleLabel = new JLabel("Student Records Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(subtitleLabel);
        titlePanel.add(titleLabel);

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Logout button for sub-menu
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> showLogoutConfirmation());

        backButton.addActionListener(e -> {
            // Properly restore the dashboard
            removeAll();
            add(dashboardPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Main content panel with black background
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(true); // Make content panel opaque
        contentPanel.setBackground(new Color(0, 0, 0, 160)); // Black background for content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Top controls panel
        JPanel controlsPanel = createStudentControlsPanel();
        
        // Student table panel
        JPanel tablePanel = createStudentTablePanel();

        contentPanel.add(controlsPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        studentRecordsPanel.add(headerPanel, BorderLayout.NORTH);
        studentRecordsPanel.add(contentPanel, BorderLayout.CENTER);

        // Replace current content
        removeAll();
        add(studentRecordsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createStudentControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setOpaque(true); // Make controls panel opaque
        panel.setBackground(new Color(0, 0, 0, 140)); // Dark background for controls

        // Add Student button
        JButton addButton = new JButton("➕ Add New Student");
        addButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); // Use emoji font
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search field with real-time search
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        addButton.addActionListener(e -> showAddStudentDialog());

        // Add real-time search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(searchField.getText()); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(searchField.getText()); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(searchField.getText()); }
        });

        panel.add(addButton);
        panel.add(new JLabel("Search:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }});
        panel.add(searchField);

        return panel;
    }

    private void performSearch(String searchText) {
        try {
            // Clear current table data
            studentTableModel.setRowCount(0);
            
            if (searchText == null || searchText.trim().isEmpty()) {
                // If search is empty, show all students
                refreshStudentTable();
                return;
            }
            
            // Load all students from database
            List<Student> allStudents = studentDao.findAll();
            String searchLower = searchText.toLowerCase().trim();
            
            // Filter students based on search text
            for (Student student : allStudents) {
                // Check if any field contains the search text
                if (student.getRollNumber().toLowerCase().contains(searchLower) ||
                    student.getName().toLowerCase().contains(searchLower) ||
                    student.getDepartment().toLowerCase().contains(searchLower) ||
                    student.getSection().toLowerCase().contains(searchLower) ||
                    student.getEmail().toLowerCase().contains(searchLower) ||
                    String.valueOf(student.getBatch()).contains(searchLower)) {
                    
                    // Add matching student to table
                    Vector<Object> row = new Vector<>();
                    row.add(student.getRollNumber());
                    row.add(student.getName());
                    row.add(student.getDepartment());
                    row.add(student.getSection());
                    row.add(student.getEmail());
                    row.add(student.getBatch());
                    row.add(""); // Actions column
                    studentTableModel.addRow(row);
                }
            }
            
            // Show search results count
            int resultCount = studentTableModel.getRowCount();
            if (resultCount == 0) {
                System.out.println("No students found matching: " + searchText);
            } else {
                System.out.println("Found " + resultCount + " students matching: " + searchText);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during search: " + e.getMessage());
        }
    }

    private void refreshStudentTable() {
        try {
            // Clear existing data
            studentTableModel.setRowCount(0);
            
            // Load data from database
            List<Student> students = studentDao.findAll();
            
            for (Student student : students) {
                Vector<Object> row = new Vector<>();
                row.add(student.getRollNumber());
                row.add(student.getName());
                row.add(student.getDepartment());
                row.add(student.getSection());
                row.add(student.getEmail());
                row.add(student.getBatch());
                row.add(""); // Actions column - will be rendered by custom renderer
                
                studentTableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createStudentTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black for better readability
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            "Student List (Double-click to view subjects)",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            Color.WHITE
        ));

        // Create table model with database data
        String[] columnNames = {"Roll Number", "Name", "Department", "Section", "Email", "Batch", "Actions"};
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Actions column is editable
            }
        };

        studentTable = new JTable(studentTableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setShowGrid(true);
        studentTable.setBackground(Color.WHITE);
        studentTable.setForeground(Color.BLACK);
        studentTable.setOpaque(true);
        
        // Set table size to prevent page-level scrollbar
        studentTable.setPreferredScrollableViewportSize(new Dimension(800, 200));

        // Add mouse listener for row clicks to show subjects
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int row = studentTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        String studentRoll = (String) studentTable.getValueAt(row, 0); // Roll Number column
                        showSubjectRecordsForStudent(studentRoll);
                    }
                }
            }
        });

        // Set custom renderer for Actions column
        studentTable.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                actionPanel.setOpaque(false);

                JButton editBtn = new JButton("✏️");
                editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                editBtn.setPreferredSize(new Dimension(30, 25));
                editBtn.setBackground(new Color(255, 193, 7));
                editBtn.setForeground(Color.BLACK);
                editBtn.setFocusPainted(false);
                editBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                JButton deleteBtn = new JButton("🗑️");
                deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                deleteBtn.setPreferredSize(new Dimension(30, 25));
                deleteBtn.setBackground(new Color(220, 53, 69));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                final int rowIndex = row;
                editBtn.addActionListener(e -> editStudent(rowIndex));
                deleteBtn.addActionListener(e -> deleteStudent(rowIndex));

                actionPanel.add(editBtn);
                actionPanel.add(deleteBtn);

                return actionPanel;
            }
        });

        // Set custom editor for Actions column
        studentTable.getColumnModel().getColumn(6).setCellEditor(new javax.swing.DefaultCellEditor(new JTextField()) {
            {
                setClickCountToStart(0);
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                actionPanel.setOpaque(false);

                JButton editBtn = new JButton("✏️");
                editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                editBtn.setPreferredSize(new Dimension(30, 25));
                editBtn.setBackground(new Color(255, 193, 7));
                editBtn.setForeground(Color.BLACK);
                editBtn.setFocusPainted(false);
                editBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                JButton deleteBtn = new JButton("🗑️");
                deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                deleteBtn.setPreferredSize(new Dimension(30, 25));
                deleteBtn.setBackground(new Color(220, 53, 69));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                final int rowIndex = row;
                editBtn.addActionListener(e -> editStudent(rowIndex));
                deleteBtn.addActionListener(e -> deleteStudent(rowIndex));

                actionPanel.add(editBtn);
                actionPanel.add(deleteBtn);

                return actionPanel;
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data from database
        refreshStudentTable();
        
        return panel;
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Add New Student", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(0, 0, 0, 200));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton saveButton = new JButton("Save Student");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        // Form panel - now we can pass the save button
        JPanel formPanel = createStudentFormPanel(dialog, saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Get form data from the form panel
                JPanel formPanelRef = (JPanel) mainPanel.getComponent(1); // Get the form panel
                
                // Extract form data (this is a simplified approach - in production you'd want better form handling)
                JTextField rollNumberField = (JTextField) formPanelRef.getComponent(1);
                JTextField nameField = (JTextField) formPanelRef.getComponent(3);
                JComboBox<String> deptComboBox = (JComboBox<String>) formPanelRef.getComponent(5);
                JComboBox<String> sectionComboBox = (JComboBox<String>) formPanelRef.getComponent(7);
                JTextField emailField = (JTextField) formPanelRef.getComponent(9);
                JComboBox<String> batchComboBox = (JComboBox<String>) formPanelRef.getComponent(11);
                
                // Create student object
                Student student = new Student();
                student.setRollNumber(rollNumberField.getText().trim());
                student.setName(nameField.getText().trim());
                student.setDepartment((String) deptComboBox.getSelectedItem());
                student.setSection((String) sectionComboBox.getSelectedItem());
                student.setEmail(emailField.getText().trim());
                student.setBatch(Integer.parseInt((String) batchComboBox.getSelectedItem()));

                // Validate fields
                if (student.getRollNumber().isEmpty() || student.getName().isEmpty() || student.getEmail().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to database
                studentDao.insert(student);
                
                // Show success message
                JOptionPane.showMessageDialog(dialog, "Student saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh table to show new data
                refreshStudentTable();
                
                // Close dialog
                dialog.dispose();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error saving student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createStudentFormPanel(JDialog dialog, JButton saveButton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        JTextField rollNumberField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JComboBox<String> deptComboBox = new JComboBox<>(new String[]{"CTECH", "CINTEL", "DSBS"});
        JComboBox<String> sectionComboBox = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2", "E1", "E2", 
                                         "F1", "F2", "G1", "G2", "H1", "H2", "I1", "I2", "J1", "J2",
                                         "K1", "K2", "L1", "L2", "M1", "M2", "N1", "N2", "O1", "O2",
                                         "P1", "P2", "Q1", "Q2", "R1", "R2", "S1", "S2", "T1", "T2"});
        JTextField emailField = new JTextField(20);
        JComboBox<String> batchComboBox = new JComboBox<>(new String[]{"2023", "2024", "2025"});

        // Set field properties
        rollNumberField.setPreferredSize(new Dimension(250, 35));
        rollNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(250, 35));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deptComboBox.setPreferredSize(new Dimension(250, 35));
        deptComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectionComboBox.setPreferredSize(new Dimension(250, 35));
        sectionComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(250, 35));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchComboBox.setPreferredSize(new Dimension(250, 35));
        batchComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Roll Number:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(rollNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Department:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(deptComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Section:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(sectionComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Batch:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(batchComboBox, gbc);

        // Add save button functionality
        // Note: Action listener is set in the calling method
        // saveButton.addActionListener(...) is handled externally

        return panel;
    }

    private void editStudent(int rowIndex) {
        try {
            // Get student data from the selected row
            String rollNumber = (String) studentTable.getValueAt(rowIndex, 0);
            String name = (String) studentTable.getValueAt(rowIndex, 1);
            String department = (String) studentTable.getValueAt(rowIndex, 2);
            String section = (String) studentTable.getValueAt(rowIndex, 3);
            String email = (String) studentTable.getValueAt(rowIndex, 4);
            Integer batchInt = (Integer) studentTable.getValueAt(rowIndex, 5);
            String batch = batchInt.toString(); // Convert Integer to String

            // Create edit dialog
            JDialog editDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit Student", Dialog.ModalityType.APPLICATION_MODAL);
            editDialog.setLayout(new BorderLayout());
            editDialog.setSize(500, 600);
            editDialog.setLocationRelativeTo(this);
            editDialog.setResizable(false);

            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(new Color(0, 0, 0, 200));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Title
            JLabel titleLabel = new JLabel("Edit Student", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);

            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            buttonPanel.setOpaque(false);

            JButton updateButton = new JButton("Update Student");
            updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            updateButton.setPreferredSize(new Dimension(140, 40));
            updateButton.setBackground(new Color(40, 167, 69));
            updateButton.setForeground(Color.WHITE);
            updateButton.setFocusPainted(false);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cancelButton.setPreferredSize(new Dimension(100, 40));
            cancelButton.setBackground(new Color(108, 117, 125));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);

            // Form panel with pre-filled data - now we can pass the update button
            JPanel formPanel = createEditStudentFormPanel(editDialog, rollNumber, name, department, section, email, batch, updateButton);

            // Add action listeners
            updateButton.addActionListener(e -> {
                try {
                    // Get form data from the form panel
                    JPanel formPanelRef = (JPanel) mainPanel.getComponent(1); // Get the form panel
                    
                    // Extract form data (this is a simplified approach - in production you'd want better form handling)
                    JTextField rollNumberField = (JTextField) formPanelRef.getComponent(1);
                    JTextField nameField = (JTextField) formPanelRef.getComponent(3);
                    JComboBox<String> deptComboBox = (JComboBox<String>) formPanelRef.getComponent(5);
                    JComboBox<String> sectionComboBox = (JComboBox<String>) formPanelRef.getComponent(7);
                    JTextField emailField = (JTextField) formPanelRef.getComponent(9);
                    JComboBox<String> batchComboBox = (JComboBox<String>) formPanelRef.getComponent(11);
                    
                    // Create updated student object
                    Student student = new Student();
                    student.setRollNumber(rollNumberField.getText().trim());
                    student.setName(nameField.getText().trim());
                    student.setDepartment((String) deptComboBox.getSelectedItem());
                    student.setSection((String) sectionComboBox.getSelectedItem());
                    student.setEmail(emailField.getText().trim());
                    student.setBatch(Integer.parseInt((String) batchComboBox.getSelectedItem()));

                    // Validate fields
                    if (student.getRollNumber().isEmpty() || student.getName().isEmpty() || student.getEmail().isEmpty()) {
                        JOptionPane.showMessageDialog(editDialog, "Please fill all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update in database
                    studentDao.update(student);
                    
                    // Show success message
                    JOptionPane.showMessageDialog(editDialog, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh table to show updated data
                    refreshStudentTable();
                    
                    // Close dialog
                    editDialog.dispose();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(editDialog, "Error updating student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(e -> editDialog.dispose());

            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);

            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            editDialog.add(mainPanel);
            editDialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createEditStudentFormPanel(JDialog dialog, String rollNumber, String name, String department, String section, String email, String batch, JButton updateButton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields with pre-filled data
        JTextField rollNumberField = new JTextField(rollNumber, 20);
        JTextField nameField = new JTextField(name, 20);
        JComboBox<String> deptComboBox = new JComboBox<>(new String[]{"CTECH", "CINTEL", "DSBS"});
        deptComboBox.setSelectedItem(department);
        JComboBox<String> sectionComboBox = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2", "E1", "E2", 
                                         "F1", "F2", "G1", "G2", "H1", "H2", "I1", "I2", "J1", "J2",
                                         "K1", "K2", "L1", "L2", "M1", "M2", "N1", "N2", "O1", "O2",
                                         "P1", "P2", "Q1", "Q2", "R1", "R2", "S1", "S2", "T1", "T2"});
        sectionComboBox.setSelectedItem(section);
        JTextField emailField = new JTextField(email, 20);
        JComboBox<String> batchComboBox = new JComboBox<>(new String[]{"2023", "2024", "2025"});
        batchComboBox.setSelectedItem(batch);

        // Set field properties
        rollNumberField.setPreferredSize(new Dimension(250, 35));
        rollNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(250, 35));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deptComboBox.setPreferredSize(new Dimension(250, 35));
        deptComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectionComboBox.setPreferredSize(new Dimension(250, 35));
        sectionComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(250, 35));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        batchComboBox.setPreferredSize(new Dimension(250, 35));
        batchComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Roll Number:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(rollNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Department:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(deptComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Section:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(sectionComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Batch:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(batchComboBox, gbc);

        // Add update button functionality
        // Note: Action listener is set in the calling method
        // updateButton.addActionListener(...) is handled externally

        return panel;
    }

    private void deleteStudent(int rowIndex) {
        try {
            // Get student roll number from the selected row
            String rollNumber = (String) studentTable.getValueAt(rowIndex, 0);
            String name = (String) studentTable.getValueAt(rowIndex, 1);

            int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student '" + name + "' (Roll: " + rollNumber + ")?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                // Delete from database
                studentDao.delete(rollNumber);
                
                JOptionPane.showMessageDialog(this, 
                    "Student '" + name + "' deleted successfully!", 
                    "Delete Student", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh table to show updated data
                refreshStudentTable();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showSubjectRecords() {
        showSubjectRecordsForStudent(null);
    }
    
    private void showSubjectRecordsForStudent(String studentRoll) {
        // Set filtering state
        if (studentRoll != null && !studentRoll.isEmpty()) {
            isFilteredByStudent = true;
            currentFilteredStudentRoll = studentRoll;
        } else {
            isFilteredByStudent = false;
            currentFilteredStudentRoll = "";
        }
        
        // Create a new panel for subject records with proper black background
        JPanel subjectRecordsPanel = new JPanel(new BorderLayout());
        subjectRecordsPanel.setOpaque(true); // Make it opaque to show background
        subjectRecordsPanel.setBackground(new Color(0, 0, 0, 180)); // More opaque black background

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(true); // Make header opaque
        headerPanel.setBackground(new Color(0, 0, 0, 200)); // Darker header background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a panel for the title and subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        
        JLabel subtitleLabel = new JLabel("Faculty Dashboard", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(135, 206, 235)); // Light blue color
        
        JLabel titleLabel;
        if (isFilteredByStudent && !currentFilteredStudentRoll.isEmpty()) {
            String studentName = getStudentNameByRoll(currentFilteredStudentRoll);
            titleLabel = new JLabel("Subject Records - " + studentName, SwingConstants.CENTER);
        } else {
            titleLabel = new JLabel("Subject Records Management", SwingConstants.CENTER);
        }
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(subtitleLabel);
        titlePanel.add(titleLabel);

        // Back button - either to dashboard or to student list
        JButton backButton;
        if (isFilteredByStudent) {
            backButton = new JButton("← Back to Student List");
            backButton.addActionListener(e -> {
                // Go back to student list
                isFilteredByStudent = false;
                currentFilteredStudentRoll = "";
                showStudentRecords();
            });
        } else {
            backButton = new JButton("← Back to Dashboard");
            backButton.addActionListener(e -> {
                // Properly restore the dashboard
                removeAll();
                add(dashboardPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            });
        }
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Logout button for sub-menu
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> showLogoutConfirmation());

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Main content panel with black background
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(true); // Make content panel opaque
        contentPanel.setBackground(new Color(0, 0, 0, 160)); // Black background for content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Top controls panel
        JPanel controlsPanel = createSubjectControlsPanel();
        
        // Subject table panel
        JPanel tablePanel = createSubjectTablePanel();

        contentPanel.add(controlsPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        subjectRecordsPanel.add(headerPanel, BorderLayout.NORTH);
        subjectRecordsPanel.add(contentPanel, BorderLayout.CENTER);

        // Replace current content
        removeAll();
        add(subjectRecordsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createSubjectControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setOpaque(true); // Make controls panel opaque
        panel.setBackground(new Color(0, 0, 0, 140)); // Dark background for controls

        // Add Subject button
        JButton addButton = new JButton("➕ Add New Subject");
        addButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); // Use emoji font
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search field with real-time search
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Semester filter dropdown
        JLabel semesterLabel = new JLabel("Semester:", SwingConstants.CENTER);
        semesterLabel.setForeground(Color.WHITE);
        semesterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JComboBox<String> semesterFilter = new JComboBox<>(new String[]{"All Semesters", "1", "2", "3", "4", "5", "6", "7", "8"});
        semesterFilter.setPreferredSize(new Dimension(150, 35));
        semesterFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        semesterFilter.setBackground(Color.WHITE);
        semesterFilter.setForeground(Color.BLACK);

        addButton.addActionListener(e -> showAddSubjectDialog());

        // Add real-time search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearchSubject(searchField.getText()); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearchSubject(searchField.getText()); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearchSubject(searchField.getText()); }
        });

        // Add semester filter functionality
        semesterFilter.addActionListener(e -> {
            String selectedSemester = (String) semesterFilter.getSelectedItem();
            if ("All Semesters".equals(selectedSemester)) {
                performSemesterFilter(null);
            } else {
                performSemesterFilter(Integer.parseInt(selectedSemester));
            }
        });

        panel.add(addButton);
        panel.add(new JLabel("Search:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }});
        panel.add(searchField);
        panel.add(semesterLabel);
        panel.add(semesterFilter);

        return panel;
    }

    private void performSearchSubject(String searchText) {
        try {
            // Clear current table data
            subjectTableModel.setRowCount(0);
            
            if (searchText == null || searchText.trim().isEmpty()) {
                // If search is empty, show all subjects
                refreshSubjectTable();
                return;
            }
            
            // Load all subjects from database
            List<Subject> allSubjects = subjectDao.findAll();
            String searchLower = searchText.toLowerCase().trim();
            
            // Apply semester filter if set
            if (currentSemesterFilter != null) {
                allSubjects = allSubjects.stream()
                    .filter(s -> s.getSemester() == currentSemesterFilter)
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Filter subjects based on search text
            List<Subject> matchingSubjects = new ArrayList<>();
            for (Subject subject : allSubjects) {
                // Check if any field contains the search text
                if (subject.getSubjectCode().toLowerCase().contains(searchLower) ||
                    subject.getSubjectName().toLowerCase().contains(searchLower) ||
                    subject.getStudentRoll().toLowerCase().contains(searchLower) ||
                    String.valueOf(subject.getCredits()).contains(searchLower) ||
                    String.valueOf(subject.getSemester()).contains(searchLower) ||
                    subject.getGrade().toLowerCase().contains(searchLower)) {
                    
                    matchingSubjects.add(subject);
                }
            }
            
            // Group matching subjects by student roll number and sort by semester within each group
            Map<String, List<Subject>> subjectsByStudent = matchingSubjects.stream()
                .collect(java.util.stream.Collectors.groupingBy(Subject::getStudentRoll));
            
            // Sort students by roll number for consistent grouping
            List<String> sortedStudentRolls = new ArrayList<>(subjectsByStudent.keySet());
            sortedStudentRolls.sort(String::compareTo);
            
            for (String studentRoll : sortedStudentRolls) {
                List<Subject> studentSubjects = subjectsByStudent.get(studentRoll);
                
                // Sort subjects by semester in increasing order
                studentSubjects.sort((s1, s2) -> Integer.compare(s1.getSemester(), s2.getSemester()));
                
                for (Subject subject : studentSubjects) {
                    // Get student name from roll number
                    String studentName = getStudentNameByRoll(subject.getStudentRoll());
                    
                    Vector<Object> row = new Vector<>();
                    row.add(studentName); // Student Name
                    row.add(subject.getSubjectName());
                    row.add(subject.getSubjectCode());
                    row.add(subject.getCredits());
                    row.add(subject.getGrade());
                    row.add(subject.getSemester());
                    row.add(""); // Actions column - will be rendered by custom renderer
                    
                    subjectTableModel.addRow(row);
                }
            }
            
            // Show search results count
            int resultCount = subjectTableModel.getRowCount();
            if (resultCount == 0) {
                System.out.println("No subjects found matching: " + searchText);
            } else {
                System.out.println("Found " + resultCount + " subjects matching: " + searchText);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during search: " + e.getMessage());
        }
    }

    private void refreshSubjectTable() {
        try {
            // Clear existing data
            subjectTableModel.setRowCount(0);
            
            // Load data from database
            List<Subject> subjects;
            if (isFilteredByStudent && !currentFilteredStudentRoll.isEmpty()) {
                // Filter by specific student
                subjects = subjectDao.findByStudentRoll(currentFilteredStudentRoll);
            } else {
                // Load all subjects
                subjects = subjectDao.findAll();
            }
            
            // Apply semester filter if set
            if (currentSemesterFilter != null) {
                subjects = subjects.stream()
                    .filter(s -> s.getSemester() == currentSemesterFilter)
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Group subjects by student roll number and sort by semester within each group
            Map<String, List<Subject>> subjectsByStudent = subjects.stream()
                .collect(java.util.stream.Collectors.groupingBy(Subject::getStudentRoll));
            
            // Sort students by roll number for consistent grouping
            List<String> sortedStudentRolls = new ArrayList<>(subjectsByStudent.keySet());
            sortedStudentRolls.sort(String::compareTo);
            
            for (String studentRoll : sortedStudentRolls) {
                List<Subject> studentSubjects = subjectsByStudent.get(studentRoll);
                
                // Sort subjects by semester in increasing order
                studentSubjects.sort((s1, s2) -> Integer.compare(s1.getSemester(), s2.getSemester()));
                
                for (Subject subject : studentSubjects) {
                    // Get student name from roll number
                    String studentName = getStudentNameByRoll(subject.getStudentRoll());
                    
                    Vector<Object> row = new Vector<>();
                    row.add(studentName); // Student Name
                    row.add(subject.getSubjectName());
                    row.add(subject.getSubjectCode());
                    row.add(subject.getCredits());
                    row.add(subject.getGrade());
                    row.add(subject.getSemester());
                    row.add(""); // Actions column - will be rendered by custom renderer
                    
                    subjectTableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createSubjectTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black for better readability
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            "Subject List",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            Color.WHITE
        ));

        // Create table model with database data
        String[] columnNames = {"Student Name", "Subject Name", "Subject Code", "Credits", "Grade", "Semester", "Actions"};
        subjectTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Actions column is editable (now column 6)
            }
        };

        subjectTable = new JTable(subjectTableModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.setRowHeight(30);
        subjectTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subjectTable.setGridColor(new Color(200, 200, 200));
        subjectTable.setShowGrid(true);
        subjectTable.setBackground(Color.WHITE);
        subjectTable.setForeground(Color.BLACK);
        subjectTable.setOpaque(true);
        
        // Set table size to prevent page-level scrollbar
        subjectTable.setPreferredScrollableViewportSize(new Dimension(800, 200));

        // Set custom renderer for Actions column
        subjectTable.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                actionPanel.setOpaque(false);

                JButton editBtn = new JButton("✏️");
                editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                editBtn.setPreferredSize(new Dimension(30, 25));
                editBtn.setBackground(new Color(255, 193, 7));
                editBtn.setForeground(Color.BLACK);
                editBtn.setFocusPainted(false);
                editBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                JButton deleteBtn = new JButton("🗑️");
                deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                deleteBtn.setPreferredSize(new Dimension(30, 25));
                deleteBtn.setBackground(new Color(220, 53, 69));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                final int rowIndex = row;
                editBtn.addActionListener(e -> editSubject(rowIndex));
                deleteBtn.addActionListener(e -> deleteSubject(rowIndex));

                actionPanel.add(editBtn);
                actionPanel.add(deleteBtn);

                return actionPanel;
            }
        });

        // Set custom editor for Actions column
        subjectTable.getColumnModel().getColumn(6).setCellEditor(new javax.swing.DefaultCellEditor(new JTextField()) {
            {
                setClickCountToStart(0);
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                actionPanel.setOpaque(false);

                JButton editBtn = new JButton("✏️");
                editBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                editBtn.setPreferredSize(new Dimension(30, 25));
                editBtn.setBackground(new Color(255, 193, 7));
                editBtn.setForeground(Color.BLACK);
                editBtn.setFocusPainted(false);
                editBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                JButton deleteBtn = new JButton("🗑️");
                deleteBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
                deleteBtn.setPreferredSize(new Dimension(30, 25));
                deleteBtn.setBackground(new Color(220, 53, 69));
                deleteBtn.setForeground(Color.WHITE);
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

                final int rowIndex = row;
                editBtn.addActionListener(e -> editSubject(rowIndex));
                deleteBtn.addActionListener(e -> deleteSubject(rowIndex));

                actionPanel.add(editBtn);
                actionPanel.add(deleteBtn);

                return actionPanel;
            }
        });

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data from database
        refreshSubjectTable();
        
        return panel;
    }
    
    private void performSemesterFilter(Integer semester) {
        currentSemesterFilter = semester;
        refreshSubjectTable();
    }
    
    private String getStudentNameByRoll(String rollNumber) {
        try {
            List<Student> students = studentDao.findAll();
            for (Student student : students) {
                if (student.getRollNumber().equals(rollNumber)) {
                    return student.getName();
                }
            }
            return "Unknown Student"; // Fallback if student not found
        } catch (Exception e) {
            e.printStackTrace();
            return "Error Loading Student";
        }
    }
    
    private String getStudentRollByName(String studentName) {
        try {
            List<Student> students = studentDao.findAll();
            for (Student student : students) {
                if (student.getName().equals(studentName)) {
                    return student.getRollNumber();
                }
            }
            return ""; // Fallback if student not found
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showAddSubjectDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Add New Subject", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(0, 0, 0, 200));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Add New Subject", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton saveButton = new JButton("Save Subject");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        // Form panel
        JPanel formPanel = createSubjectFormPanel(dialog, saveButton);

        saveButton.addActionListener(e -> {
            try {
                // Get form data from the form panel
                JPanel formPanelRef = (JPanel) mainPanel.getComponent(1); // Get the form panel
                
                // Extract form data
                JComboBox<Student> studentCombo = (JComboBox<Student>) formPanelRef.getComponent(1);
                JTextField subjectNameField = (JTextField) formPanelRef.getComponent(3);
                JTextField subjectCodeField = (JTextField) formPanelRef.getComponent(5);
                JComboBox<Integer> creditsCombo = (JComboBox<Integer>) formPanelRef.getComponent(7);
                JComboBox<String> gradeCombo = (JComboBox<String>) formPanelRef.getComponent(9);
                JComboBox<Integer> semesterCombo = (JComboBox<Integer>) formPanelRef.getComponent(11);
                
                // Get selected student
                Student selectedStudent = (Student) studentCombo.getSelectedItem();
                if (selectedStudent == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select a student!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create subject object
                Subject subject = new Subject();
                subject.setStudentRoll(selectedStudent.getRollNumber());
                subject.setSubjectName(subjectNameField.getText().trim());
                subject.setSubjectCode(subjectCodeField.getText().trim());
                subject.setCredits((Integer) creditsCombo.getSelectedItem());
                subject.setGrade((String) gradeCombo.getSelectedItem());
                subject.setSemester((Integer) semesterCombo.getSelectedItem());

                // Validate fields
                if (subject.getSubjectName().isEmpty() || subject.getSubjectCode().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to database
                subjectDao.insert(subject);
                
                // Show success message
                JOptionPane.showMessageDialog(dialog, "Subject saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh table to show new data
                refreshSubjectTable();
                
                // Close dialog
                dialog.dispose();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error saving subject: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createSubjectFormPanel(JDialog dialog, JButton saveButton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields
        JComboBox<Student> studentCombo = new JComboBox<>();
        JTextField subjectNameField = new JTextField(20);
        JTextField subjectCodeField = new JTextField(20);
        JComboBox<Integer> creditsCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"O", "A+", "A", "B+", "B", "C+", "C", "F"});
        JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});

        // Load students into combo box
        try {
            List<Student> students = studentDao.findAll();
            for (Student student : students) {
                studentCombo.addItem(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set field properties
        studentCombo.setPreferredSize(new Dimension(250, 35));
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectNameField.setPreferredSize(new Dimension(250, 35));
        subjectNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectCodeField.setPreferredSize(new Dimension(250, 35));
        subjectCodeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        creditsCombo.setPreferredSize(new Dimension(250, 35));
        creditsCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeCombo.setPreferredSize(new Dimension(250, 35));
        gradeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        semesterCombo.setPreferredSize(new Dimension(250, 35));
        semesterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(studentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Subject Name:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(subjectNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Subject Code:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(subjectCodeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Credits:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(creditsCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Grade:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(gradeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Semester:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(semesterCombo, gbc);

        return panel;
    }

    private void editSubject(int rowIndex) {
        try {
            // Get subject data from the selected row (new column structure)
            String studentName = (String) subjectTable.getValueAt(rowIndex, 0); // Student Name
            String subjectName = (String) subjectTable.getValueAt(rowIndex, 1); // Subject Name
            String subjectCode = (String) subjectTable.getValueAt(rowIndex, 2); // Subject Code
            Integer credits = (Integer) subjectTable.getValueAt(rowIndex, 3); // Credits
            String grade = (String) subjectTable.getValueAt(rowIndex, 4); // Grade
            Integer semester = (Integer) subjectTable.getValueAt(rowIndex, 5); // Semester
            
            // Get student roll number from student name
            String studentRoll = getStudentRollByName(studentName);

            // Create edit dialog
            JDialog editDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit Subject", Dialog.ModalityType.APPLICATION_MODAL);
            editDialog.setLayout(new BorderLayout());
            editDialog.setSize(500, 600);
            editDialog.setLocationRelativeTo(this);
            editDialog.setResizable(false);

            JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
            mainPanel.setBackground(new Color(0, 0, 0, 200));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Title
            JLabel titleLabel = new JLabel("Edit Subject", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);

            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            buttonPanel.setOpaque(false);

            JButton updateButton = new JButton("Update Subject");
            updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            updateButton.setPreferredSize(new Dimension(140, 40));
            updateButton.setBackground(new Color(40, 167, 69));
            updateButton.setForeground(Color.WHITE);
            updateButton.setFocusPainted(false);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cancelButton.setPreferredSize(new Dimension(100, 40));
            cancelButton.setBackground(new Color(108, 117, 125));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setFocusPainted(false);

            // Form panel with pre-filled data
            JPanel formPanel = createEditSubjectFormPanel(editDialog, studentRoll, subjectName, subjectCode, credits, grade, semester, updateButton);

            // Add action listeners
            updateButton.addActionListener(e -> {
                try {
                    // Get form data from the form panel
                    JPanel formPanelRef = (JPanel) mainPanel.getComponent(1); // Get the form panel
                    
                    // Extract form data
                    JComboBox<Student> studentCombo = (JComboBox<Student>) formPanelRef.getComponent(1);
                    JTextField subjectNameField = (JTextField) formPanelRef.getComponent(3);
                    JTextField subjectCodeField = (JTextField) formPanelRef.getComponent(5);
                    JComboBox<Integer> creditsCombo = (JComboBox<Integer>) formPanelRef.getComponent(7);
                    JComboBox<String> gradeCombo = (JComboBox<String>) formPanelRef.getComponent(9);
                    JComboBox<Integer> semesterCombo = (JComboBox<Integer>) formPanelRef.getComponent(11);
                    
                    // Get selected student
                    Student selectedStudent = (Student) studentCombo.getSelectedItem();
                    if (selectedStudent == null) {
                        JOptionPane.showMessageDialog(editDialog, "Please select a student!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                                         // Create updated subject object
                     Subject subject = new Subject();
                     subject.setStudentRoll(selectedStudent.getRollNumber());
                    subject.setSubjectName(subjectNameField.getText().trim());
                    subject.setSubjectCode(subjectCodeField.getText().trim());
                    subject.setCredits((Integer) creditsCombo.getSelectedItem());
                    subject.setGrade((String) gradeCombo.getSelectedItem());
                    subject.setSemester((Integer) semesterCombo.getSelectedItem());

                    // Validate fields
                    if (subject.getSubjectName().isEmpty() || subject.getSubjectCode().isEmpty()) {
                        JOptionPane.showMessageDialog(editDialog, "Please fill all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update in database
                    subjectDao.update(subject);
                    
                    // Show success message
                    JOptionPane.showMessageDialog(editDialog, "Subject updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh table to show updated data
                    refreshSubjectTable();
                    
                    // Close dialog
                    editDialog.dispose();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(editDialog, "Error updating subject: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(e -> editDialog.dispose());

            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);

            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            editDialog.add(mainPanel);
            editDialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing subject: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createEditSubjectFormPanel(JDialog dialog, String studentRoll, String subjectName, String subjectCode, Integer credits, String grade, Integer semester, JButton updateButton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Form fields with pre-filled data
        JComboBox<Student> studentCombo = new JComboBox<>();
        JTextField subjectNameField = new JTextField(subjectName, 20);
        JTextField subjectCodeField = new JTextField(subjectCode, 20);
        JComboBox<Integer> creditsCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"O", "A+", "A", "B+", "B", "C+", "C", "F"});
        JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});

        // Load students into combo box and select the current one
        try {
            List<Student> students = studentDao.findAll();
            for (Student student : students) {
                studentCombo.addItem(student);
                if (student.getRollNumber().equals(studentRoll)) {
                    studentCombo.setSelectedItem(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set selected values
        creditsCombo.setSelectedItem(credits);
        gradeCombo.setSelectedItem(grade);
        semesterCombo.setSelectedItem(semester);

        // Set field properties
        studentCombo.setPreferredSize(new Dimension(250, 35));
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectNameField.setPreferredSize(new Dimension(250, 35));
        subjectNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectCodeField.setPreferredSize(new Dimension(250, 35));
        subjectCodeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        creditsCombo.setPreferredSize(new Dimension(250, 35));
        creditsCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeCombo.setPreferredSize(new Dimension(250, 35));
        gradeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        semesterCombo.setPreferredSize(new Dimension(250, 35));
        semesterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(studentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Subject Name:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(subjectNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Subject Code:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(subjectCodeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Credits:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(creditsCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Grade:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(gradeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Semester:", SwingConstants.CENTER) {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }}, gbc);
        gbc.gridx = 1;
        panel.add(semesterCombo, gbc);

        return panel;
    }

    private void deleteSubject(int rowIndex) {
        try {
            // Get subject data from the selected row (new column structure)
            String studentName = (String) subjectTable.getValueAt(rowIndex, 0); // Student Name
            String subjectName = (String) subjectTable.getValueAt(rowIndex, 1); // Subject Name
            String subjectCode = (String) subjectTable.getValueAt(rowIndex, 2); // Subject Code
            
            // Get student roll number from student name
            String studentRoll = getStudentRollByName(studentName);

            int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subject '" + subjectName + "'?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (result == JOptionPane.YES_OPTION) {
                // Find and delete the subject from database
                try {
                    List<Subject> subjects = subjectDao.findByStudentRoll(studentRoll);
                    for (Subject subject : subjects) {
                        if (subject.getSubjectName().equals(subjectName) && 
                            subject.getSubjectCode().equals(subjectCode)) {
                            subjectDao.delete(subject.getId());
                            break;
                        }
                    }
                    
                    JOptionPane.showMessageDialog(this, 
                        "Subject '" + subjectName + "' deleted successfully!", 
                        "Delete Subject", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh table to show updated data
                    refreshSubjectTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting subject: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting subject: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showResultRecords() {
        // Create a new panel for result records with proper black background
        JPanel resultRecordsPanel = new JPanel(new BorderLayout());
        resultRecordsPanel.setOpaque(true); // Make it opaque to show background
        resultRecordsPanel.setBackground(new Color(0, 0, 0, 180)); // More opaque black background

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(true); // Make header opaque
        headerPanel.setBackground(new Color(0, 0, 0, 200)); // Darker header background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Faculty Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Logout button for sub-menu
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> showLogoutConfirmation());

        backButton.addActionListener(e -> showDashboardPanel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.WEST);

        // Main content panel with CGPA calculation functionality
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create the CGPA calculation panel
        JPanel cgpaCalculationPanel = createAdvancedCgpaCalculationPanel();
        mainContentPanel.add(cgpaCalculationPanel, BorderLayout.CENTER);

        resultRecordsPanel.add(headerPanel, BorderLayout.NORTH);
        resultRecordsPanel.add(mainContentPanel, BorderLayout.CENTER);

        removeAll();
        add(resultRecordsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    // Chart data for visualization
    private Map<Integer, Double> semesterGpa = new HashMap<>();
    private double overallCgpa = 0.0;
    private boolean hasData = false;
    private AdvancedCgpaPieChartPanel lastTwoSemestersChart;
    private AdvancedCgpaPieChartPanel individualSemestersChart;
    private AdvancedCgpaPieChartPanel overallCgpaChart;
    private JLabel cgpaLabel;
    private JLabel classificationLabel;
    
    private JPanel createAdvancedCgpaCalculationPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        
        // Filter Section
        JPanel filterPanel = createAdvancedCgpaFilterPanel();
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Charts Section
        JPanel chartsPanel = createAdvancedCgpaChartsPanel();
        panel.add(chartsPanel, BorderLayout.CENTER);
        
        // Results Section
        JPanel resultsPanel = createAdvancedCgpaResultsPanel();
        panel.add(resultsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCgpaCalculationPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        
        // Filter Section
        JPanel filterPanel = createCgpaFilterPanel();
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Charts Section
        JPanel chartsPanel = createCgpaChartsPanel();
        panel.add(chartsPanel, BorderLayout.CENTER);
        
        // Results Section
        JPanel resultsPanel = createCgpaResultsPanel();
        panel.add(resultsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCgpaFilterPanel() {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Student Filter & CGPA Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        card.add(titleLabel, gbc);
        
        // Section and Batch selection
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; card.add(new JLabel("Section:"), gbc);
        JComboBox<String> sectionCombo = new JComboBox<>(buildSections());
        sectionCombo.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1; card.add(sectionCombo, gbc);
        gbc.gridx = 2; card.add(new JLabel("Batch:"), gbc);
        JComboBox<Integer> batchCombo = new JComboBox<>(buildBatches());
        batchCombo.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 3; card.add(batchCombo, gbc);
        
        // Student selection
        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Student:"), gbc);
        JComboBox<Student> studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(studentCombo, gbc);
        gbc.gridwidth = 1;
        
        // Calculate Button
        JButton calcBtn = new JButton("Calculate CGPA");
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setBackground(new Color(231, 76, 60));
        calcBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        calcBtn.setFocusPainted(false);
        gbc.gridx = 3; gbc.gridy = 2;
        card.add(calcBtn, gbc);
        
        // Add action listeners
        sectionCombo.addActionListener(e -> refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo));
        batchCombo.addActionListener(e -> refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo));
        calcBtn.addActionListener(e -> calculateCgpa(studentCombo));
        
        // Initialize students
        refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo);
        
        return card;
    }
    
    private JPanel createCgpaChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        chartsPanel.setOpaque(false);
        
        // SGPA Chart 1 (Last 2 semesters)
        chartsPanel.add(createCgpaChartPanel("SGPA - Last 2 Semesters", true));
        
        // SGPA Chart 2 (Individual semesters)
        chartsPanel.add(createCgpaChartPanel("SGPA - Individual Semesters", false));
        
        // Overall CGPA Chart
        chartsPanel.add(createCgpaChartPanel("Overall CGPA", false));
        
        return chartsPanel;
    }
    
    private JPanel createCgpaChartPanel(String title, boolean isLastTwoSemesters) {
        JPanel chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBackground(new Color(255, 255, 255, 220));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        chartPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        CgpaPieChartPanel pieChart = new CgpaPieChartPanel(isLastTwoSemesters);
        pieChart.setPreferredSize(new Dimension(200, 200));
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(pieChart, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createCgpaResultsPanel() {
        JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        results.setOpaque(false);
        
        JLabel cgpaTextLabel = new JLabel("CGPA:");
        cgpaTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        cgpaTextLabel.setForeground(new Color(52, 73, 94));
        
        JLabel cgpaLabel = new JLabel("0.00");
        cgpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        cgpaLabel.setForeground(new Color(46, 204, 113));
        cgpaLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        JLabel classificationLabel = new JLabel("N/A");
        classificationLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        classificationLabel.setForeground(new Color(52, 152, 219));
        
        results.add(cgpaTextLabel);
        results.add(cgpaLabel);
        results.add(classificationLabel);
        
        return results;
    }
    
    private void refreshStudentsForCgpa(JComboBox<Student> studentCombo, JComboBox<String> sectionCombo, JComboBox<Integer> batchCombo) {
        studentCombo.removeAllItems();
        String selectedSection = (String) sectionCombo.getSelectedItem();
        Integer selectedBatch = (Integer) batchCombo.getSelectedItem();
        if (selectedSection != null && selectedBatch != null) {
            List<Student> students = studentDao.findBySectionAndBatch(selectedSection, selectedBatch);
            for (Student s : students) {
                studentCombo.addItem(s);
            }
        }
    }
    
    private void calculateCgpa(JComboBox<Student> studentCombo) {
        Student s = (Student) studentCombo.getSelectedItem();
        if (s == null || s.getRollNumber() == null) {
            JOptionPane.showMessageDialog(this, "Select a student");
            return;
        }
        
        // Calculate overall CGPA
        CgpaService cgpaService = new CgpaService(subjectDao);
        double overallCgpa = cgpaService.computeCgpaForStudent(s.getRollNumber());
        
        // Calculate semester-wise GPA
        Map<Integer, Double> semesterGpa = calculateSemesterGpa(s.getRollNumber());
        
        // Update display
        DecimalFormat df = new DecimalFormat("0.00");
        // Note: In a real implementation, you would need to update the labels in the results panel
        // For now, we'll show the results in a dialog
        String result = "Student: " + s.getName() + " (" + s.getRollNumber() + ")\n";
        result += "Overall CGPA: " + df.format(overallCgpa) + "\n";
        result += "Classification: " + classify(overallCgpa) + "\n\n";
        result += "Semester-wise SGPA:\n";
        for (Map.Entry<Integer, Double> entry : semesterGpa.entrySet()) {
            result += "Semester " + entry.getKey() + ": " + df.format(entry.getValue()) + "\n";
        }
        
        JOptionPane.showMessageDialog(this, result, "CGPA Calculation Results", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Map<Integer, Double> calculateSemesterGpa(String rollNumber) {
        Map<Integer, Double> semesterGpa = new HashMap<>();
        List<Subject> subjects = subjectDao.findByStudentRoll(rollNumber);
        
        Map<Integer, List<Subject>> semesterSubjects = new HashMap<>();
        for (Subject subject : subjects) {
            semesterSubjects.computeIfAbsent(subject.getSemester(), k -> new ArrayList<>()).add(subject);
        }
        
        for (Map.Entry<Integer, List<Subject>> entry : semesterSubjects.entrySet()) {
            int semester = entry.getKey();
            List<Subject> semesterSubs = entry.getValue();
            
            double totalCredits = 0.0;
            double weightedSum = 0.0;
            
            for (Subject sub : semesterSubs) {
                double points = CgpaService.gradeToPoint(sub.getGrade());
                totalCredits += sub.getCredits();
                weightedSum += points * sub.getCredits();
            }
            
            if (totalCredits > 0) {
                double sgpa = weightedSum / totalCredits;
                semesterGpa.put(semester, sgpa);
            }
        }
        
        return semesterGpa;
    }
    
    private String classify(double cgpa) {
        if (cgpa >= 9) return "Outstanding";
        if (cgpa >= 8) return "Excellent";
        if (cgpa >= 7) return "Very Good";
        if (cgpa >= 6) return "Good";
        if (cgpa >= 5) return "Pass";
        return "Fail";
    }
    
    private static String[] buildSections() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (char c = 'A'; c <= 'T'; c++) {
            list.add(c + "1");
        }
        for (char c = 'A'; c <= 'T'; c++) {
            list.add(c + "2");
        }
        return list.toArray(new String[0]);
    }

    private static Integer[] buildBatches() {
        return new Integer[]{2023, 2024, 2025};
    }
    
    // Advanced CGPA Filter Panel with enhanced functionality
    private JPanel createAdvancedCgpaFilterPanel() {
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("📊 Results Records");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Center the title
        card.add(titleLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST; // Reset anchor for other components
        
        // Section and Batch selection
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; 
        JLabel sectionLabel = new JLabel("📚 Section:");
        sectionLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        sectionLabel.setForeground(Color.WHITE);
        card.add(sectionLabel, gbc);
        
        JComboBox<String> sectionCombo = new JComboBox<>(buildSections());
        sectionCombo.setPreferredSize(new Dimension(120, 35));
        sectionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; card.add(sectionCombo, gbc);
        
        gbc.gridx = 2; 
        gbc.insets = new Insets(8, 5, 8, 0); // Minimal right inset for Batch label
        JLabel batchLabel = new JLabel("📅 Batch:");
        batchLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        batchLabel.setForeground(Color.WHITE);
        card.add(batchLabel, gbc);
        
        gbc.insets = new Insets(8, 0, 8, 8); // Minimal left inset for batch dropdown
        JComboBox<Integer> batchCombo = new JComboBox<>(buildBatches());
        batchCombo.setPreferredSize(new Dimension(100, 35));
        batchCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 3; card.add(batchCombo, gbc);
        
        // Student selection
        gbc.gridx = 0; gbc.gridy = 2; 
        JLabel studentLabel = new JLabel("👤 Student:");
        studentLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        studentLabel.setForeground(Color.WHITE);
        card.add(studentLabel, gbc);
        
        JComboBox<Student> studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(300, 35));
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridwidth = 2; card.add(studentCombo, gbc);
        gbc.gridwidth = 1;
        
        // Calculate Button with enhanced styling
        JButton calcBtn = new JButton("🧮 Calculate CGPA");
        calcBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setBackground(new Color(231, 76, 60));
        calcBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        calcBtn.setFocusPainted(false);
        calcBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 3; gbc.gridy = 2;
        card.add(calcBtn, gbc);
        
        // Add action listeners
        sectionCombo.addActionListener(e -> refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo));
        batchCombo.addActionListener(e -> refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo));
        calcBtn.addActionListener(e -> calculateAdvancedCgpa(studentCombo));
        
        // Initialize students
        refreshStudentsForCgpa(studentCombo, sectionCombo, batchCombo);
        
        return card;
    }
    
    private JPanel createAdvancedCgpaChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        chartsPanel.setOpaque(false);
        
        // SGPA Chart 1 (Last 2 semesters)
        JPanel lastTwoPanel = createAdvancedCgpaChartPanel("📊 SGPA - Last 2 Semesters", true);
        chartsPanel.add(lastTwoPanel);
        
        // SGPA Chart 2 (Individual semesters)
        JPanel individualPanel = createAdvancedCgpaChartPanel("📈 SGPA - Individual Semesters", false);
        chartsPanel.add(individualPanel);
        
        // Overall CGPA Chart
        JPanel overallPanel = createAdvancedCgpaChartPanel("🎯 Overall CGPA", false);
        chartsPanel.add(overallPanel);
        
        return chartsPanel;
    }
    
    private JPanel createAdvancedCgpaChartPanel(String title, boolean isLastTwoSemesters) {
        JPanel chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBackground(new Color(255, 255, 255, 220));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        chartPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        AdvancedCgpaPieChartPanel pieChart = new AdvancedCgpaPieChartPanel(isLastTwoSemesters);
        pieChart.setPreferredSize(new Dimension(200, 200));
        
        // Store references to chart panels
        if (title.contains("Last 2 Semesters")) {
            lastTwoSemestersChart = pieChart;
        } else if (title.contains("Individual Semesters")) {
            individualSemestersChart = pieChart;
        } else if (title.contains("Overall CGPA")) {
            overallCgpaChart = pieChart;
        }
        
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        chartPanel.add(pieChart, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createAdvancedCgpaResultsPanel() {
        JPanel results = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        results.setOpaque(false);
        
        JLabel cgpaTextLabel = new JLabel("🎓 CGPA:");
        cgpaTextLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        cgpaTextLabel.setForeground(Color.WHITE);
        
        cgpaLabel = new JLabel("0.00");
        cgpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        cgpaLabel.setForeground(new Color(46, 204, 113));
        cgpaLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        
        classificationLabel = new JLabel("N/A");
        classificationLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        classificationLabel.setForeground(new Color(52, 152, 219));
        
        results.add(cgpaTextLabel);
        results.add(cgpaLabel);
        results.add(classificationLabel);
        
        return results;
    }
    
    private void calculateAdvancedCgpa(JComboBox<Student> studentCombo) {
        Student s = (Student) studentCombo.getSelectedItem();
        if (s == null || s.getRollNumber() == null) {
            JOptionPane.showMessageDialog(this, "Please select a student to calculate CGPA", "No Student Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Calculate overall CGPA
            CgpaService cgpaService = new CgpaService(subjectDao);
            overallCgpa = cgpaService.computeCgpaForStudent(s.getRollNumber());
            
            // Calculate semester-wise GPA
            semesterGpa = calculateSemesterGpa(s.getRollNumber());
            
            // Update the data flag
            hasData = true;
            
            // Update display labels (format without unnecessary trailing zeros)
            DecimalFormat df = new DecimalFormat("#.##");
            cgpaLabel.setText(df.format(overallCgpa));
            classificationLabel.setText(classify(overallCgpa));
            
            // Update chart panels with data
            if (lastTwoSemestersChart != null) {
                lastTwoSemestersChart.setData(semesterGpa, overallCgpa, hasData);
            }
            if (individualSemestersChart != null) {
                individualSemestersChart.setData(semesterGpa, overallCgpa, hasData);
            }
            if (overallCgpaChart != null) {
                overallCgpaChart.setData(semesterGpa, overallCgpa, hasData);
            }
            
            // Refresh all charts and UI
            if (lastTwoSemestersChart != null) lastTwoSemestersChart.repaint();
            if (individualSemestersChart != null) individualSemestersChart.repaint();
            if (overallCgpaChart != null) overallCgpaChart.repaint();
            revalidate();
            repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating CGPA: " + e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    // Custom Pie Chart Panel for CGPA visualization
    private class CgpaPieChartPanel extends JPanel {
        private final boolean isLastTwoSemesters;
        
        public CgpaPieChartPanel(boolean isLastTwoSemesters) {
            this.isLastTwoSemesters = isLastTwoSemesters;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawNoDataMessage(g);
        }
        
        private void drawNoDataMessage(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(150, 150, 150));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            String message = "Click Calculate CGPA";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2;
            
            g2d.drawString(message, x, y);
            g2d.dispose();
        }
    }
    
    // Advanced Pie Chart Panel for CGPA visualization
    private class AdvancedCgpaPieChartPanel extends JPanel {
        private final boolean isLastTwoSemesters;
        private Map<Integer, Double> semesterGpa = new HashMap<>();
        private double overallCgpa = 0.0;
        private boolean hasData = false;
        
        public AdvancedCgpaPieChartPanel(boolean isLastTwoSemesters) {
            this.isLastTwoSemesters = isLastTwoSemesters;
            setOpaque(false);
        }
        
        public void setData(Map<Integer, Double> semesterGpa, double overallCgpa, boolean hasData) {
            this.semesterGpa = new HashMap<>(semesterGpa);
            this.overallCgpa = overallCgpa;
            this.hasData = hasData;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!hasData) {
                drawEnhancedNoDataMessage(g);
                return;
            }
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 20;
            
            if (isLastTwoSemesters) {
                drawLastTwoSemestersChart(g2d, centerX, centerY, radius);
            } else if (getTitle().contains("Individual")) {
                drawIndividualSemestersChart(g2d, centerX, centerY, radius);
            } else {
                drawOverallCgpaChart(g2d, centerX, centerY, radius);
            }
            
            g2d.dispose();
        }
        
        private void drawEnhancedNoDataMessage(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            
            String message = "🧮 Click Calculate CGPA";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2;
            
            g2d.drawString(message, x, y);
            g2d.dispose();
        }
        
        private void drawLastTwoSemestersChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            List<Map.Entry<Integer, Double>> sortedSemesters = semesterGpa.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByKey().reversed())
                .limit(2)
                .toList();
            
            if (sortedSemesters.isEmpty()) return;
            
            double total = sortedSemesters.stream().mapToDouble(Map.Entry::getValue).sum();
            double startAngle = 0;
            
            Color[] colors = {new Color(52, 152, 219), new Color(231, 76, 60)};
            int colorIndex = 0;
            
            for (Map.Entry<Integer, Double> entry : sortedSemesters) {
                double percentage = entry.getValue() / total;
                double sweepAngle = percentage * 360;
                
                g2d.setColor(colors[colorIndex % colors.length]);
                g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                    radius * 2, radius * 2, startAngle, sweepAngle, Arc2D.PIE));
                
                // Draw semester label
                drawPieLabel(g2d, centerX, centerY, radius, startAngle + sweepAngle/2, 
                    "Sem " + entry.getKey() + "\n" + String.format("%.2f", entry.getValue()));
                
                startAngle += sweepAngle;
                colorIndex++;
            }
        }
        
        private void drawIndividualSemestersChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            if (semesterGpa.isEmpty()) return;
            
            double total = semesterGpa.values().stream().mapToDouble(Double::doubleValue).sum();
            double startAngle = 0;
            
            Color[] colors = {new Color(52, 152, 219), new Color(231, 76, 60), 
                             new Color(46, 204, 113), new Color(155, 89, 182),
                             new Color(241, 196, 15), new Color(230, 126, 34)};
            int colorIndex = 0;
            
            for (Map.Entry<Integer, Double> entry : semesterGpa.entrySet()) {
                double percentage = entry.getValue() / total;
                double sweepAngle = percentage * 360;
                
                g2d.setColor(colors[colorIndex % colors.length]);
                g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                    radius * 2, radius * 2, startAngle, sweepAngle, Arc2D.PIE));
                
                // Draw semester label
                drawPieLabel(g2d, centerX, centerY, radius, startAngle + sweepAngle/2, 
                    "Sem " + entry.getKey() + "\n" + String.format("%.2f", entry.getValue()));
                
                startAngle += sweepAngle;
                colorIndex++;
            }
        }
        
        private void drawOverallCgpaChart(Graphics2D g2d, int centerX, int centerY, int radius) {
            if (overallCgpa == 0) return;
            
            // Create a donut chart showing CGPA out of 10
            double percentage = overallCgpa / 10.0;
            double sweepAngle = percentage * 360;
            
            // Background circle (gray)
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
            
            // CGPA arc (blue)
            g2d.setColor(new Color(52, 152, 219));
            g2d.fill(new Arc2D.Double(centerX - radius, centerY - radius, 
                radius * 2, radius * 2, -90, sweepAngle, Arc2D.PIE));
            
            // Center text
            g2d.setColor(new Color(52, 73, 94));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            String cgpaText = String.format("%.2f", overallCgpa);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(cgpaText);
            int x = centerX - textWidth / 2;
            int y = centerY + fm.getAscent() / 2;
            g2d.drawString(cgpaText, x, y);
        }
        
        private void drawPieLabel(Graphics2D g2d, int centerX, int centerY, int radius, 
                                 double angle, String text) {
            double radian = Math.toRadians(angle);
            int labelRadius = radius * 3 / 4;
            int x = centerX + (int) (labelRadius * Math.cos(radian));
            int y = centerY - (int) (labelRadius * Math.sin(radian));
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
            
            String[] lines = text.split("\n");
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();
            
            for (int i = 0; i < lines.length; i++) {
                int textWidth = fm.stringWidth(lines[i]);
                int textX = x - textWidth / 2;
                int textY = y - (lines.length - 1) * lineHeight / 2 + i * lineHeight;
                g2d.drawString(lines[i], textX, textY);
            }
        }
        
        private String getTitle() {
            Container parent = getParent();
            if (parent instanceof JPanel) {
                Component[] components = ((JPanel) parent).getComponents();
                for (Component comp : components) {
                    if (comp instanceof JLabel) {
                        return ((JLabel) comp).getText();
                    }
                }
            }
            return "";
        }
    }
} 