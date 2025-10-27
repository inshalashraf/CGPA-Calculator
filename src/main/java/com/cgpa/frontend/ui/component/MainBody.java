package com.cgpa.frontend.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import java.awt.Font;

public class MainBody extends javax.swing.JPanel {

    private JPanel panelTitle;
    private JLabel lbTitle;
    private JScrollPane scroll;
    private JPanel panelBody;

    public MainBody() {
        initComponents();
        setOpaque(false); // Make transparent to show background image
        scroll.setBorder(null);
        scroll.setViewportBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        // scroll.setVerticalScrollBar(new ScrollBarCustom()); // We'll add this later if needed
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void displayForm(Component form) {
        displayForm(form, "");
    }

    public void displayForm(Component form, String title) {
        lbTitle.setText(title);
        
        // First, deactivate the current form if it exists
        if (panelBody.getComponentCount() > 0) {
            Component currentForm = panelBody.getComponent(0);
            if (currentForm instanceof Form) {
                Form oldForm = (Form) currentForm;
                oldForm.setActive(false);
            }
        }
        
        // Clear the panel body completely
        panelBody.removeAll();
        
        // Add the new form
        panelBody.add(form);
        
        // Force multiple repaints on everything to clear any cached content
        panelBody.repaint();
        panelBody.revalidate();
        scroll.repaint();
        scroll.revalidate();
        this.repaint();
        this.revalidate();
        
        // Force another round of repaints after a short delay
        javax.swing.SwingUtilities.invokeLater(() -> {
            panelBody.repaint();
            scroll.repaint();
            this.repaint();
        });
        
        // Set this form as active
        if (form instanceof Form) {
            Form activeForm = (Form) form;
            activeForm.setActive(true);
        }
    }

    public void changeColor(Color color) {
        lbTitle.setForeground(color);
        if (panelBody.getComponentCount() != 0) {
            Form com = (Form) panelBody.getComponent(0);
            com.changeColor(color);
        }
    }

    private void initComponents() {
        panelTitle = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        panelBody = new javax.swing.JPanel();

        panelTitle.setOpaque(false);

        lbTitle.setFont(new Font("sansserif", Font.BOLD, 18));
        lbTitle.setForeground(new Color(255, 255, 255));

        GroupLayout panelTitleLayout = new GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, panelTitleLayout.createSequentialGroup()
                .addContainerGap(867, Short.MAX_VALUE)
                .addComponent(lbTitle)
                .addContainerGap())
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addContainerGap())
        );

        panelBody.setOpaque(false);
        panelBody.setLayout(new java.awt.BorderLayout());
        scroll.setViewportView(panelBody);
        scroll.setOpaque(false);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroll)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
        );
    }
} 