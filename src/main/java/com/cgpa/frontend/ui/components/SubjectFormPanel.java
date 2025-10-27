package com.cgpa.frontend.ui.components;

import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.dao.SubjectDao;
import com.cgpa.backend.model.Student;
import com.cgpa.backend.model.Subject;
import com.cgpa.frontend.events.StudentEventBus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubjectFormPanel extends JPanel {
    private final JComboBox<Student> studentCombo;
    private final JTextField subjectNameField = new JTextField();
    private final JTextField subjectCodeField = new JTextField();
    private final JComboBox<Integer> creditsCombo = new JComboBox<>(new Integer[]{0,1,2,3,4,5});
    private final JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"O","A+","A","B+","B","C+","C","F"});
    private final JComboBox<Integer> semesterCombo = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8});

    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Semester", "Roll Number", "Subject Name", "Subject Code", "Credits", "Grade"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final StudentDao studentDao;
    private final SubjectDao subjectDao;
    private Integer selectedId = null;
    private Object[] selectedRowData = null; // Added for update/delete operations

    public SubjectFormPanel(StudentDao studentDao, SubjectDao subjectDao) {
        this.studentDao = studentDao;
        this.subjectDao = subjectDao;
        this.studentCombo = new JComboBox<>();
        setLayout(new BorderLayout(8, 8));
        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshStudents();
        refreshTable();
        table.getSelectionModel().addListSelectionListener(e -> onRowSelect());
        StudentEventBus.register(this::refreshStudents);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(255, 255, 255, 220));
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        form.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;
        int r = 0;
        
        addRow(form, gbc, r++, "Student:", studentCombo);
        addRow(form, gbc, r++, "Subject Name:", subjectNameField);
        addRow(form, gbc, r++, "Subject Code:", subjectCodeField);
        addRow(form, gbc, r++, "Credits:", creditsCombo);
        addRow(form, gbc, r++, "Grade:", gradeCombo);
        addRow(form, gbc, r++, "Semester:", semesterCombo);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Subject");
        JButton updateBtn = new JButton("Update Subject");
        JButton deleteBtn = new JButton("Delete Subject");
        JButton clearBtn = new JButton("Clear Form");

        addBtn.setBackground(new Color(52, 152, 219));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        updateBtn.setBackground(new Color(46, 204, 113));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        clearBtn.setBackground(new Color(149, 165, 166));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        addBtn.addActionListener(e -> addSubject());
        updateBtn.addActionListener(e -> updateSubject());
        deleteBtn.addActionListener(e -> deleteSubject());
        clearBtn.addActionListener(e -> clearForm());

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2; gbc.weightx = 1.0;
        form.add(buttons, gbc);
        return form;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(new Color(52, 73, 94));
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(labelComponent, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void refreshStudents() {
        studentCombo.removeAllItems();
        for (Student s : studentDao.findAll()) {
            studentCombo.addItem(s);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Subject s : subjectDao.findAll()) {
            tableModel.addRow(new Object[]{s.getSemester(), s.getStudentRoll(), s.getSubjectName(), s.getSubjectCode(), s.getCredits(), s.getGrade()});
        }
    }

    private void onRowSelect() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            // Get semester from first column (index 0)
            semesterCombo.setSelectedItem(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 0))));
            // Get roll number from second column (index 1)
            String roll = String.valueOf(tableModel.getValueAt(row, 1));
            selectStudentInCombo(roll);
            // Get subject name from third column (index 2)
            subjectNameField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            // Get subject code from fourth column (index 3)
            subjectCodeField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            // Get credits from fifth column (index 4)
            creditsCombo.setSelectedItem(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 4))));
            // Get grade from sixth column (index 5)
            gradeCombo.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 5)));
            
            // Store the selected row data for update/delete operations
            selectedRowData = new Object[]{
                tableModel.getValueAt(row, 0), // semester
                tableModel.getValueAt(row, 1), // roll number
                tableModel.getValueAt(row, 2), // subject name
                tableModel.getValueAt(row, 3), // subject code
                tableModel.getValueAt(row, 4), // credits
                tableModel.getValueAt(row, 5)  // grade
            };
        }
    }

    private void selectStudentInCombo(String roll) {
        ComboBoxModel<Student> model = studentCombo.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Student s = model.getElementAt(i);
            if (s.getRollNumber() != null && s.getRollNumber().equals(roll)) {
                studentCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void addSubject() {
        Student selected = (Student) studentCombo.getSelectedItem();
        if (selected == null || selected.getRollNumber() == null) {
            JOptionPane.showMessageDialog(this, "Select a valid student");
            return;
        }
        Subject s = new Subject(null,
                selected.getRollNumber(),
                subjectNameField.getText().trim(),
                subjectCodeField.getText().trim(),
                (int) creditsCombo.getSelectedItem(),
                String.valueOf(gradeCombo.getSelectedItem()),
                (int) semesterCombo.getSelectedItem());
        if (s.getSubjectName().isEmpty() || s.getSubjectCode().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Subject name and code are required");
            return;
        }
        subjectDao.insert(s);
        clearForm();
        refreshTable();
    }

    private void updateSubject() {
        if (selectedRowData == null) {
            JOptionPane.showMessageDialog(this, "Select a subject to update");
            return;
        }
        
        Student selected = (Student) studentCombo.getSelectedItem();
        if (selected == null || selected.getRollNumber() == null) {
            JOptionPane.showMessageDialog(this, "Select a valid student");
            return;
        }
        
        // Find the existing subject to get its ID for update
        List<Subject> existingSubjects = subjectDao.findByStudentRoll(selected.getRollNumber());
        Subject existingSubject = null;
        
        for (Subject s : existingSubjects) {
            if (s.getSubjectName().equals(selectedRowData[2]) && 
                s.getSubjectCode().equals(selectedRowData[3]) &&
                s.getSemester() == (Integer) selectedRowData[0]) {
                existingSubject = s;
                break;
            }
        }
        
        if (existingSubject == null) {
            JOptionPane.showMessageDialog(this, "Subject not found for update");
            return;
        }
        
        // Create updated subject with existing ID
        Subject updatedSubject = new Subject(
            existingSubject.getId(),
            selected.getRollNumber(),
            subjectNameField.getText().trim(),
            subjectCodeField.getText().trim(),
            (int) creditsCombo.getSelectedItem(),
            String.valueOf(gradeCombo.getSelectedItem()),
            (int) semesterCombo.getSelectedItem()
        );
        
        subjectDao.update(updatedSubject);
        clearForm();
        refreshTable();
    }

    private void deleteSubject() {
        if (selectedRowData == null) {
            JOptionPane.showMessageDialog(this, "Select a subject to delete");
            return;
        }
        
        // Find the existing subject to get its ID for deletion
        List<Subject> existingSubjects = subjectDao.findByStudentRoll((String) selectedRowData[1]);
        Subject existingSubject = null;
        
        for (Subject s : existingSubjects) {
            if (s.getSubjectName().equals(selectedRowData[2]) && 
                s.getSubjectCode().equals(selectedRowData[3]) &&
                s.getSemester() == (Integer) selectedRowData[0]) {
                existingSubject = s;
                break;
            }
        }
        
        if (existingSubject == null) {
            JOptionPane.showMessageDialog(this, "Subject not found for deletion");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected subject?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            subjectDao.delete(existingSubject.getId());
            clearForm();
            refreshTable();
        }
    }

    private void clearForm() {
        selectedId = null;
        subjectNameField.setText("");
        subjectCodeField.setText("");
        creditsCombo.setSelectedIndex(0);
        gradeCombo.setSelectedIndex(0);
        semesterCombo.setSelectedIndex(0);
        table.clearSelection();
        selectedRowData = null; // Clear selected row data
    }
}

