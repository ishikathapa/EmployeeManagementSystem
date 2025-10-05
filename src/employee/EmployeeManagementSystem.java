package employee;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Comparator;
import java.io.File;

public class EmployeeManagementSystem {

    public static LinkedList<Employee> employees = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Employee Management System =====");
            System.out.println("1. Text-Based Interface (TBI)");
            System.out.println("2. Graphical User Interface (GUI)");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> launchTBI();
                case "2" -> launchGUI();
                case "3" -> {
                    System.out.println("Exiting program...");
                    System.exit(0);
                }
                default -> System.out.println("⚠ Invalid option, please try again.");
            }
        }
    }

    private static void launchGUI() {
        SwingUtilities.invokeLater(() -> new EMS_GUI(employees));
    }

    private static void launchTBI() {
        while (true) {
            System.out.println("\n===== TBI Menu =====");
            System.out.println("1. Load Employees");
            System.out.println("2. Add Employee");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. View Employees");
            System.out.println("6. Search Employee");
            System.out.println("7. Sort by Name");
            System.out.println("8. Manage Performance/Salary");
            System.out.println("9. Save Employees");
            System.out.println("10. Return to Main Menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> loadEmployeesTBI();
                case "2" -> addEmployeeTBI();
                case "3" -> updateEmployeeTBI();
                case "4" -> deleteEmployeeTBI();
                case "5" -> viewEmployeesTBI(employees);
                case "6" -> searchEmployeesTBI();
                case "7" -> sortEmployeesTBI();
                case "8" -> managePerformanceAndSalaryTBI();
                case "9" -> saveEmployeesTBI();
                case "10" -> { 
                    System.out.println("Returning to main menu...");
                    return; 
                }
                default -> System.out.println("⚠ Invalid choice. Try again.");
            }
        }
    }

    // =================== TBI Functions ===================
    private static void loadEmployeesTBI() {
        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("⚠ File not found!");
            return;
        }
        employees = FileHandler.loadEmployees(filename);
        System.out.println("✅ Loaded " + employees.size() + " employees.");
    }

    private static void saveEmployeesTBI() {
        System.out.print("Enter filename to save: ");
        String filename = scanner.nextLine();
        FileHandler.saveEmployees(filename, employees);
        System.out.println("✅ Employees saved successfully.");
    }

    private static void addEmployeeTBI() {
        System.out.print("Enter type (Manager/Intern/Regular): ");
        String type = scanner.nextLine();

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (findEmployeeById(id) != null) {
            System.out.println("⚠ Employee ID already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine();
        System.out.print("Enter Base Salary: ");
        double salary = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        Employee emp = switch (type.toLowerCase()) {
            case "manager" -> new Manager(id, name, dept, salary, rating);
            case "intern" -> new Intern(id, name, dept, salary, rating);
            case "regular" -> new Regular(id, name, dept, salary, rating);
            default -> null;
        };

        if (emp == null) {
            System.out.println("⚠ Invalid employee type.");
            return;
        }

        employees.add(emp);
        System.out.println("✅ Employee added successfully.");
    }

    private static void updateEmployeeTBI() {
        System.out.print("Enter ID to update: ");
        Employee emp = findEmployeeById(scanner.nextLine());
        if (emp == null) { System.out.println("⚠ Employee not found."); return; }

        System.out.print("New Name (" + emp.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) emp.setName(name);

        System.out.print("New Department (" + emp.getDepartment() + "): ");
        String dept = scanner.nextLine();
        if (!dept.isBlank()) emp.setDepartment(dept);

        System.out.print("New Salary (" + emp.getSalary() + "): ");
        String sal = scanner.nextLine();
        if (!sal.isBlank()) emp.setSalary(Double.parseDouble(sal));

        System.out.print("New Rating (" + emp.getPerformanceRating() + "): ");
        String rat = scanner.nextLine();
        if (!rat.isBlank()) emp.setPerformanceRating(Integer.parseInt(rat));

        System.out.println("✅ Employee updated successfully.");
    }

    private static void deleteEmployeeTBI() {
        System.out.print("Enter ID to delete: ");
        Employee emp = findEmployeeById(scanner.nextLine());
        if (emp != null) { employees.remove(emp); System.out.println("✅ Employee deleted successfully."); }
        else System.out.println("⚠ Employee not found.");
    }

    private static void viewEmployeesTBI(LinkedList<Employee> list) {
        if (list.isEmpty()) { System.out.println("⚠ No employees found."); return; }
        System.out.println("\n===== Employee Records =====");
        for (Employee emp : list) { System.out.println(emp); }
    }

    private static void searchEmployeesTBI() {
        System.out.print("Enter name keyword: ");
        String key = scanner.nextLine().toLowerCase();
        LinkedList<Employee> result = new LinkedList<>();
        for (Employee emp : employees) { if (emp.getName().toLowerCase().contains(key)) result.add(emp); }
        viewEmployeesTBI(result);
    }

    private static void sortEmployeesTBI() {
        employees.sort(Comparator.comparing(Employee::getName));
        System.out.println("✅ Employees sorted by name.");
    }

    private static void managePerformanceAndSalaryTBI() {
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        Employee emp = findEmployeeById(id);
        if (emp == null) { System.out.println("⚠ Employee not found."); return; }

        if (emp.getPerformanceRating() <= 2) System.out.println("⚠ Warning Letter issued to " + emp.getName());
        else if (emp.getPerformanceRating() >= 4) System.out.println("🏅 Appreciation Letter issued to " + emp.getName());
        else System.out.println(emp.getName() + "'s performance is satisfactory.");

        System.out.println("Do you want to adjust salary? (1 = Yes, 2 = No)");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.println("Choose action: 1 - Add Bonus  |  2 - Apply Fine");
            String action = scanner.nextLine();
            try {
                System.out.print("Enter amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                if (action.equals("1")) { emp.setSalary(emp.getSalary() + amount); System.out.println("💰 Bonus added. New salary: " + emp.getSalary()); }
                else if (action.equals("2")) { emp.setSalary(emp.getSalary() - amount); System.out.println("⚠ Fine applied. New salary: " + emp.getSalary()); }
                else System.out.println("Invalid action.");
            } catch (Exception e) { System.out.println("Invalid input for amount."); }
        } else System.out.println("Salary adjustment skipped.");
    }

    private static Employee findEmployeeById(String id) {
        for (Employee emp : employees) { if (emp.getId().equals(id)) return emp; }
        return null;
    }
}
