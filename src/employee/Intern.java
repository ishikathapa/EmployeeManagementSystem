package employee;

// Intern gets 50% bonus
public class Intern extends Employee {

    public Intern(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    @Override
    public double calculateSalary() {
        return salary + (salary * 0.50); // 50% bonus
    }
}
