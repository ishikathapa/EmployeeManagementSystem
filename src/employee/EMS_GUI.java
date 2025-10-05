package employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;

public class EMS_GUI extends JFrame {

    private LinkedList<Employee> employees; // Reference to main employee list
    private DefaultTableModel tableModel;

    public EMS_GUI(LinkedList<Employee> employees) {
        this.employees = employees;
        initializeGUI();
    }

    private void initializeGUI() {
        // ===== Frame Settings =====
        setTitle("Employee Management System");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Main Panel =====
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        // ===== Title =====
        JLabel title = new JLabel("Employee Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(0, 102, 204));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== Buttons Panel =====
        JPanel buttonPanel = new JPanel(new GridLayout(2, 5, 12, 12));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Buttons
        JButton addBtn = new JButton("Add Employee");
        JButton viewBtn = new JButton("View Employees");
        JButton updateBtn = new JButton("Update Employee");
        JButton deleteBtn = new JButton("Delete Employee");
        JButton searchBtn = new JButton("Search Employee");
        JButton sortBtn = new JButton("Sort by Name");
        JButton loadBtn = new JButton("Load Employees");
        JButton saveBtn = new JButton("Save Employees");
        JButton managePerfBtn = new JButton("Manage Performance/Salary");
        JButton exitBtn = new JButton("Exit"); // New Exit button

        JButton[] buttons = {addBtn, viewBtn, updateBtn, deleteBtn, searchBtn, sortBtn, loadBtn, saveBtn, managePerfBtn, exitBtn};
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.PLAIN, 14));
            b.setBackground(new Color(102, 204, 255));
            b.setForeground(Color.BLACK);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
            buttonPanel.add(b);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== Table =====
        String[] columns = {"ID", "Name", "Department", "Base Salary", "Rating", "Bonus", "Total Salary", "Performance"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== Button Actions =====
        addBtn.addActionListener(e -> addEmployee());
        viewBtn.addActionListener(e -> refreshTable());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        searchBtn.addActionListener(e -> searchEmployee());
        sortBtn.addActionListener(e -> sortEmployees());
        loadBtn.addActionListener(e -> loadEmployees());
        saveBtn.addActionListener(e -> saveEmployees());

        managePerfBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "⚠ Please select an employee first.");
                return;
            }
            Employee emp = employees.get(selectedRow);
            new ManagePerformanceDialog(emp, this);
        });

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    // ===== GUI Methods =====
    private void addEmployee() {
        String type = JOptionPane.showInputDialog(this, "Enter type (Manager/Intern/Regular):");
        String id = JOptionPane.showInputDialog(this, "Enter ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Name:");
        String dept = JOptionPane.showInputDialog(this, "Enter Department:");
        double salary = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Base Salary:"));
        int rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Rating (1-5):"));

        Employee emp = switch (type.toLowerCase()) {
            case "manager" -> new Manager(id, name, dept, salary, rating);
            case "intern" -> new Intern(id, name, dept, salary, rating);
            case "regular" -> new Regular(id, name, dept, salary, rating);
            default -> null;
        };

        if (emp != null) {
            employees.add(emp);
            JOptionPane.showMessageDialog(this, "✅ Employee added!");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "⚠ Invalid employee type.");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0); // Clear table
        for (Employee e : employees) {
            double bonus = e.calculateSalary() - e.getSalary();
            String performanceLetter = getPerformanceLetter(e.getPerformanceRating());
            tableModel.addRow(new Object[]{
                    e.getId(), e.getName(), e.getDepartment(), e.getSalary(),
                    e.getPerformanceRating(), bonus, e.calculateSalary(), performanceLetter
            });
        }
    }

    private void updateEmployee() {
        String id = JOptionPane.showInputDialog(this, "Enter ID to update:");
        Employee emp = findEmployeeById(id);
        if (emp == null) { JOptionPane.showMessageDialog(this, "⚠ Employee not found."); return; }

        String name = JOptionPane.showInputDialog(this, "New Name (leave blank to keep):");
        if (!name.isBlank()) emp.setName(name);

        String dept = JOptionPane.showInputDialog(this, "New Department (leave blank to keep):");
        if (!dept.isBlank()) emp.setDepartment(dept);

        String sal = JOptionPane.showInputDialog(this, "New Salary (leave blank to keep):");
        if (!sal.isBlank()) emp.setSalary(Double.parseDouble(sal));

        String rat = JOptionPane.showInputDialog(this, "New Rating 1-5 (leave blank to keep):");
        if (!rat.isBlank()) emp.setPerformanceRating(Integer.parseInt(rat));

        JOptionPane.showMessageDialog(this, "✅ Employee updated!");
        refreshTable();
    }

    private void deleteEmployee() {
        String id = JOptionPane.showInputDialog(this, "Enter ID to delete:");
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            employees.remove(emp);
            JOptionPane.showMessageDialog(this, "✅ Employee deleted!");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "⚠ Employee not found.");
        }
    }

    private void searchEmployee() {
        String key = JOptionPane.showInputDialog(this, "Enter Name keyword to search:").toLowerCase();
        LinkedList<Employee> result = new LinkedList<>();
        for (Employee e : employees) {
            if (e.getName().toLowerCase().contains(key)) result.add(e);
        }
        if (result.isEmpty()) JOptionPane.showMessageDialog(this, "⚠ No employees found.");
        else {
            tableModel.setRowCount(0);
            for (Employee e : result) {
                double bonus = e.calculateSalary() - e.getSalary();
                String performanceLetter = getPerformanceLetter(e.getPerformanceRating());
                tableModel.addRow(new Object[]{
                        e.getId(), e.getName(), e.getDepartment(), e.getSalary(),
                        e.getPerformanceRating(), bonus, e.calculateSalary(), performanceLetter
                });
            }
        }
    }

    private void sortEmployees() {
        employees.sort(Comparator.comparing(Employee::getName));
        JOptionPane.showMessageDialog(this, "✅ Employees sorted by Name.");
        refreshTable();
    }

    private void loadEmployees() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename to load:");
        employees = FileHandler.loadEmployees(filename);
        JOptionPane.showMessageDialog(this, "✅ Loaded " + employees.size() + " employees.");
        refreshTable();
    }

    private void saveEmployees() {
        String filename = JOptionPane.showInputDialog(this, "Enter filename to save:");
        FileHandler.saveEmployees(filename, employees);
        JOptionPane.showMessageDialog(this, "✅ Employees saved successfully.");
    }

    // ===== Helper Methods =====
    private Employee findEmployeeById(String id) {
        for (Employee emp : employees) if (emp.getId().equals(id)) return emp;
        return null;
    }

    private String getPerformanceLetter(int rating) {
        if (rating <= 2) return "Warning Letter";
        else if (rating == 3) return "Satisfactory";
        else return "Appreciation Letter";
    }
}
