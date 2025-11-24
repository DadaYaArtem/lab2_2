package com.pizzeria.model.users;

import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.exceptions.EmployeeNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс менеджера
 */
public class Manager extends Employee {
    private List<Employee> managedEmployees;
    private String department;

    public Manager(String id, String firstName, String lastName, double salary) {
        super(id, firstName, lastName, EmployeeRole.MANAGER, salary);
        this.managedEmployees = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Менеджер";
    }

    @Override
    public void performDuty() {
        System.out.println(getFullName() + " управляет пиццерией");
    }

    public void addEmployee(Employee employee) {
        managedEmployees.add(employee);
        System.out.println(getFullName() + " добавил сотрудника: " + employee.getFullName());
    }

    public void removeEmployee(Employee employee) throws EmployeeNotFoundException {
        if (!managedEmployees.remove(employee)) {
            throw new EmployeeNotFoundException(employee.getId());
        }
        System.out.println(getFullName() + " удалил сотрудника: " + employee.getFullName());
    }

    public double calculateTotalPayroll() {
        double total = 0;
        for (Employee emp : managedEmployees) {
            total += emp.getSalary();
        }
        return total;
    }

    public List<Employee> getEmployeesByRole(EmployeeRole role) {
        List<Employee> result = new ArrayList<>();
        for (Employee emp : managedEmployees) {
            if (emp.getRoleEnum() == role) {
                result.add(emp);
            }
        }
        return result;
    }

    public void conductPerformanceReview(Employee employee) {
        System.out.println(getFullName() + " проводит оценку сотрудника: " + employee.getFullName());
    }

    // Getters and Setters
    public List<Employee> getManagedEmployees() { return managedEmployees; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
