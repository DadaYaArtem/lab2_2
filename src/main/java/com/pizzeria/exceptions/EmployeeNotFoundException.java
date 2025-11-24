package com.pizzeria.exceptions;

/**
 * Исключение когда сотрудник не найден
 */
public class EmployeeNotFoundException extends Exception {
    private String employeeId;

    public EmployeeNotFoundException(String employeeId) {
        super("Сотрудник с ID " + employeeId + " не найден");
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
