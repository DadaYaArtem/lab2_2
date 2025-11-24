package com.pizzeria.model.users;

import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.exceptions.InvalidAuthenticationException;
import com.pizzeria.interfaces.Authenticatable;

import java.time.LocalDate;

/**
 * Абстрактный класс сотрудника
 */
public abstract class Employee extends Person implements Authenticatable {
    protected EmployeeRole role;
    protected double salary;
    protected LocalDate hireDate;
    protected String password;
    protected boolean isActive;

    public Employee(String id, String firstName, String lastName, EmployeeRole role, double salary) {
        super(id, firstName, lastName);
        this.role = role;
        this.salary = salary;
        this.hireDate = LocalDate.now();
        this.password = "default123"; // дефолтный пароль
        this.isActive = true;
    }

    @Override
    public boolean authenticate(String username, String password) throws InvalidAuthenticationException {
        if (!isActive) {
            throw new InvalidAuthenticationException(username, "Аккаунт неактивен");
        }
        if (!this.id.equals(username)) {
            throw new InvalidAuthenticationException(username);
        }
        return verifyPassword(password);
    }

    @Override
    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) throws InvalidAuthenticationException {
        if (!verifyPassword(oldPassword)) {
            throw new InvalidAuthenticationException(id, "Неверный старый пароль");
        }
        this.password = newPassword;
    }

    public abstract void performDuty();

    public int getYearsOfService() {
        return LocalDate.now().getYear() - hireDate.getYear();
    }

    public double calculateBonus() {
        return salary * 0.1 * getYearsOfService();
    }

    // Getters and Setters
    public EmployeeRole getRoleEnum() { return role; }
    public void setRoleEnum(EmployeeRole role) { this.role = role; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
