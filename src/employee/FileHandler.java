package employee;

import java.io.*;
import java.util.LinkedList;

/**
 * Handles loading and saving employee data
 */
public class FileHandler {

    // Load employees from CSV
    public static LinkedList<Employee> loadEmployees(String filename) {
        LinkedList<Employee> employees = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 6) continue; // skip invalid lines

                String type = parts[0];
                String id = parts[1];
                String name = parts[2];
                String dept = parts[3];
                double salary = Double.parseDouble(parts[4]);
                int rating = Integer.parseInt(parts[5]);

                Employee emp = switch(type) {
                    case "Manager" -> new Manager(id, name, dept, salary, rating);
                    case "Intern" -> new Intern(id, name, dept, salary, rating);
                    case "Regular" -> new Regular(id, name, dept, salary, rating);
                    default -> null;
                };
                if(emp != null) employees.add(emp);
            }
        } catch(Exception e) {
            System.out.println("⚠ Error loading file: " + e.getMessage());
        }
        return employees;
    }

    // Save employees to CSV
    public static void saveEmployees(String filename, LinkedList<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for(Employee emp : employees) {
                writer.write(emp.toCSV());
                writer.newLine();
            }
            System.out.println("✅ Data saved to " + filename);
        } catch(Exception e) {
            System.out.println("⚠ Error saving file: " + e.getMessage());
        }
    }
}
