package employee;

// Manager gets 10% bonus
public class Manager extends Employee {

    public Manager(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    @Override
    public double calculateSalary() {
        return salary + (salary * 0.10); // 10% bonus
    }
}
