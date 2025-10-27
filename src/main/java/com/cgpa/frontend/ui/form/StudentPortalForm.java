package com.cgpa.frontend.ui.form;

import com.cgpa.frontend.ui.component.Form;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentPortalForm extends Form {

	private JLabel cgpaLabel;
	private JPanel coursesPanel;
	private List<CourseRow> courseRows;
	private JButton addCourseButton;

	// Grade point mapping
	private static final java.util.Map<String, Double> GRADE_POINTS = new java.util.HashMap<>();
	static {
		GRADE_POINTS.put("O", 10.0);
		GRADE_POINTS.put("A+", 9.0);
		GRADE_POINTS.put("A", 8.0);
		GRADE_POINTS.put("B+", 7.0);
		GRADE_POINTS.put("B", 6.0);
		GRADE_POINTS.put("C+", 5.0);
		GRADE_POINTS.put("C", 4.0);
		GRADE_POINTS.put("F", 0.0);
		GRADE_POINTS.put("W", 0.0);
		GRADE_POINTS.put("Abs/Det", 0.0);
	}

	public StudentPortalForm() {
		courseRows = new ArrayList<>();
		initComponents();
	}

	@Override
	public void changeColor(Color color) {
		// Update colors when theme changes
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			repaint();
			revalidate();
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setOpaque(false);

		JPanel cgpaPanel = createCgpaCalculatorPanel();
		add(cgpaPanel, BorderLayout.CENTER);
	}

	private JPanel createCgpaCalculatorPanel() {
		JPanel panel = new JPanel(new BorderLayout(30, 30));
		panel.setBackground(new Color(0, 0, 0, 120));
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		JPanel cgpaDisplayPanel = createCgpaDisplayPanel();
		coursesPanel = createCoursesPanel();
		JPanel buttonPanel = createAddCourseButtonPanel();

		panel.add(cgpaDisplayPanel, BorderLayout.NORTH);
		panel.add(coursesPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		for (int i = 0; i < 5; i++) {
			addCourseRow();
		}

		return panel;
	}

	private JPanel createCgpaDisplayPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		panel.setOpaque(false);

		cgpaLabel = new JLabel("0.00");
		cgpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
		cgpaLabel.setForeground(new Color(144, 238, 144));

		JLabel cgpaTextLabel = new JLabel("CGPA");
		cgpaTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		cgpaTextLabel.setForeground(new Color(200, 200, 200));

		panel.add(cgpaLabel);
		panel.add(cgpaTextLabel);

		return panel;
	}

	private JPanel createCoursesPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);

		JPanel headerPanel = createTableHeader();
		JPanel coursesContainer = new JPanel();
		coursesContainer.setLayout(new BoxLayout(coursesContainer, BoxLayout.Y_AXIS));
		coursesContainer.setOpaque(false);

		panel.add(headerPanel, BorderLayout.NORTH);
		panel.add(coursesContainer, BorderLayout.CENTER);

		this.coursesPanel = coursesContainer;

		return panel;
	}

	private JPanel createTableHeader() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panel.setOpaque(false);

		JLabel snoLabel = new JLabel("S.no");
		snoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		snoLabel.setForeground(Color.WHITE);
		snoLabel.setPreferredSize(new Dimension(60, 30));

		JLabel credLabel = new JLabel("Cred");
		credLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		credLabel.setForeground(Color.WHITE);
		credLabel.setPreferredSize(new Dimension(80, 30));

		JLabel gradeLabel = new JLabel("Grade");
		gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		gradeLabel.setForeground(Color.WHITE);
		gradeLabel.setPreferredSize(new Dimension(80, 30));

		panel.add(snoLabel);
		panel.add(credLabel);
		panel.add(gradeLabel);

		return panel;
	}

	private JPanel createAddCourseButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setOpaque(false);

		addCourseButton = new JButton("+ Add a Course");
		addCourseButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		addCourseButton.setPreferredSize(new Dimension(200, 45));
		addCourseButton.setBackground(new Color(100, 100, 100));
		addCourseButton.setForeground(Color.WHITE);
		addCourseButton.setBorderPainted(false);
		addCourseButton.setFocusPainted(false);
		addCourseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		addCourseButton.addActionListener(e -> addCourseRow());

		panel.add(addCourseButton);

		return panel;
	}

	private void addCourseRow() {
		CourseRow courseRow = new CourseRow(courseRows.size() + 1);
		courseRows.add(courseRow);
		coursesPanel.add(courseRow);
		coursesPanel.revalidate();
		coursesPanel.repaint();
		calculateCGPA();
	}

	private void calculateCGPA() {
		double totalGradePoints = 0.0;
		double totalCredits = 0.0;

		for (CourseRow row : courseRows) {
			try {
				double credits = Double.parseDouble(row.getCredits());
				String grade = row.getGrade();

				if (credits > 0 && GRADE_POINTS.containsKey(grade)) {
					totalGradePoints += credits * GRADE_POINTS.get(grade);
					totalCredits += credits;
				}
			} catch (NumberFormatException e) {
				// Skip invalid credit values
			}
		}

		if (totalCredits > 0) {
			double cgpa = totalGradePoints / totalCredits;
			cgpaLabel.setText(String.format("%.2f", cgpa));
		} else {
			cgpaLabel.setText("0.00");
		}
	}

	private class CourseRow extends JPanel {
		private JLabel snoLabel;
		private JTextField creditsField;
		private JComboBox<String> gradeCombo;
		private int rowNumber;

		public CourseRow(int rowNumber) {
			this.rowNumber = rowNumber;
			setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
			setOpaque(false);
			setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

			snoLabel = new JLabel(rowNumber + ".");
			snoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			snoLabel.setForeground(Color.WHITE);
			snoLabel.setPreferredSize(new Dimension(60, 30));

			creditsField = new JTextField("0");
			creditsField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			creditsField.setPreferredSize(new Dimension(80, 35));
			creditsField.setHorizontalAlignment(JTextField.CENTER);
			creditsField.setBackground(new Color(60, 60, 60));
			creditsField.setForeground(Color.WHITE);
			creditsField.setCaretColor(Color.WHITE);
			creditsField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)
			));

			gradeCombo = new JComboBox<>(new String[]{"O", "A+", "A", "B+", "B", "C+", "C", "F", "W", "Abs/Det"});
			gradeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			gradeCombo.setPreferredSize(new Dimension(80, 35));
			gradeCombo.setBackground(new Color(60, 60, 60));
			gradeCombo.setForeground(Color.WHITE);
			gradeCombo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(100, 100, 100), 2, true),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)
			));

			creditsField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) { calculateCGPA(); }
				@Override
				public void removeUpdate(DocumentEvent e) { calculateCGPA(); }
				@Override
				public void changedUpdate(DocumentEvent e) { calculateCGPA(); }
			});

			gradeCombo.addActionListener(e -> calculateCGPA());

			add(snoLabel);
			add(creditsField);
			add(gradeCombo);
		}

		public String getCredits() {
			return creditsField.getText();
		}

		public String getGrade() {
			return (String) gradeCombo.getSelectedItem();
		}

		public void setRowNumber(int newNumber) {
			this.rowNumber = newNumber;
			snoLabel.setText(newNumber + ".");
		}
	}
}

