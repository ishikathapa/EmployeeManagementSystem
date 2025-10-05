package employee;

import javax.swing.*;
import java.awt.*;

public class ManagePerformanceDialog extends JDialog {

    public ManagePerformanceDialog(Employee emp, EMS_GUI parentGUI) {
        setTitle("Manage Performance & Salary - " + emp.getName());
        setSize(400, 250);
        setLayout(new GridLayout(0, 1, 10, 10));
        setLocationRelativeTo(null);
        setModal(true);

        // Performance Letter
        JLabel lblPerformance = new JLabel();
        int rating = emp.getPerformanceRating();
        if (rating <= 2) {
            lblPerformance.setText("⚠ Warning Letter issued to " + emp.getName());
        } else if (rating >= 4) {
            lblPerformance.setText("🏅 Appreciation Letter issued to " + emp.getName());
        } else {
            lblPerformance.setText(emp.getName() + "'s performance is satisfactory.");
        }
        add(lblPerformance);

        // Salary Label
        JLabel lblSalary = new JLabel("Current Salary: " + emp.getSalary());
        add(lblSalary);

        // Adjust Salary Button
        JButton btnAdjustSalary = new JButton("Adjust Salary");
        btnAdjustSalary.addActionListener(e -> adjustSalary(emp, lblSalary, parentGUI));
        add(btnAdjustSalary);

        // Close Button
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose);

        setVisible(true);
    }

    private void adjustSalary(Employee emp, JLabel lblSalary, EMS_GUI parentGUI) {
        String[] options = {"Add Bonus", "Apply Fine"};
        int action = JOptionPane.showOptionDialog(
                this,
                "Choose action:",
                "Salary Adjustment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (action == -1) return; // Cancelled

        String input = JOptionPane.showInputDialog(this, "Enter amount:");
        if (input == null || input.isBlank()) return;

        try {
            double amount = Double.parseDouble(input);
            if (action == 0) {
                emp.setSalary(emp.getSalary() + amount);
                JOptionPane.showMessageDialog(this, "💰 Bonus added. New Salary: " + emp.getSalary());
            } else {
                emp.setSalary(emp.getSalary() - amount);
                JOptionPane.showMessageDialog(this, "⚠ Fine applied. New Salary: " + emp.getSalary());
            }
            lblSalary.setText("Current Salary: " + emp.getSalary());
            parentGUI.refreshTable(); // Update table automatically
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Invalid amount entered.");
        }
    }
}
