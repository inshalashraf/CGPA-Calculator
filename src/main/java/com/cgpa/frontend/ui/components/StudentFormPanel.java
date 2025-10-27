package com.cgpa.frontend.ui.components;

import com.cgpa.backend.dao.StudentDao;
import com.cgpa.backend.model.Student;
import com.cgpa.frontend.events.StudentEventBus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentFormPanel extends JPanel {
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField rollField = new JTextField();
    private final JComboBox<String> departmentCombo = new JComboBox<>(new String[]{"CTECH","CINTEL","DSBS"});
    private final JComboBox<String> sectionCombo = new JComboBox<>(buildSections());
    private final JSpinner batchSpinner = new JSpinner(new SpinnerNumberModel(2024, 1990, 2099, 1));

    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Roll Number", "Name", "Email", "Department", "Section", "Batch"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final StudentDao studentDao;
    private Integer selectedId = null;

    public StudentFormPanel(StudentDao studentDao) {
        this.studentDao = studentDao;
        setLayout(new BorderLayout(8, 8));
        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshTable();
        table.getSelectionModel().addListSelectionListener(e -> onRowSelect());
        StudentEventBus.register(this::refreshTable);
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
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.3;

        int r = 0;
        addRow(form, gbc, r++, "Name:", nameField);
        addRow(form, gbc, r++, "Email:", emailField);
        addRow(form, gbc, r++, "Roll Number:", rollField);
        addRow(form, gbc, r++, "Department:", departmentCombo);
        addRow(form, gbc, r++, "Section:", sectionCombo);
        addRow(form, gbc, r++, "Batch:", batchSpinner);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Student");
        JButton updateBtn = new JButton("Update Student");
        JButton deleteBtn = new JButton("Delete Student");
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

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentDao.findAll();
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getRollNumber(), s.getName(), s.getEmail(), s.getDepartment(), s.getSection(), s.getBatch()});
        }
    }

    private void onRowSelect() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String roll = String.valueOf(tableModel.getValueAt(row, 0));
            rollField.setText(roll);
            nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
            emailField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            departmentCombo.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 3)));
            sectionCombo.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 4)));
            batchSpinner.setValue(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 5))));
        }
    }

    private void addStudent() {
        Student s = new Student(
                nameField.getText().trim(),
                emailField.getText().trim(),
                rollField.getText().trim(),
                String.valueOf(departmentCombo.getSelectedItem()),
                String.valueOf(sectionCombo.getSelectedItem()),
                (int) batchSpinner.getValue());
        if (s.getName().isEmpty() || s.getRollNumber().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Roll Number are required");
            return;
        }
        studentDao.insert(s);
        clearForm();
        refreshTable();
        StudentEventBus.fireStudentsChanged();
    }

    private void updateStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter roll number to update");
            return;
        }
        Student s = new Student(
                nameField.getText().trim(),
                emailField.getText().trim(),
                roll,
                String.valueOf(departmentCombo.getSelectedItem()),
                String.valueOf(sectionCombo.getSelectedItem()),
                (int) batchSpinner.getValue());
        studentDao.update(s);
        clearForm();
        refreshTable();
        StudentEventBus.fireStudentsChanged();
    }

    private void deleteStudent() {
        String roll = rollField.getText().trim();
        if (roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter roll number to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete student with roll " + roll + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            studentDao.delete(roll);
            clearForm();
            refreshTable();
            StudentEventBus.fireStudentsChanged();
        }
    }

    private void clearForm() {
        selectedId = null;
        nameField.setText("");
        emailField.setText("");
        rollField.setText("");
        departmentCombo.setSelectedIndex(0);
        sectionCombo.setSelectedIndex(0);
        batchSpinner.setValue(2024);
        table.clearSelection();
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
}

