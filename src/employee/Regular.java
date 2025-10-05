package employee;

// Regular employee, no bonus
public class Regular extends Employee {

    public Regular(String id, String name, String department, double salary, int performanceRating) {
        super(id, name, department, salary, performanceRating);
    }

    @Override
    public double calculateSalary() {
        return salary; // no bonus
    }
}
