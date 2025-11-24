package com.pizzeria.model;

import com.pizzeria.model.users.Employee;
import com.pizzeria.model.users.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс пиццерии
 */
public class Pizzeria {
    private String name;
    private Address address;
    private List<Employee> employees;
    private Manager manager;
    private Kitchen kitchen;
    private Menu menu;
    private List<Order> orderHistory;
    private boolean isOpen;
    private String workingHours;

    public Pizzeria(String name, Address address) {
        this.name = name;
        this.address = address;
        this.employees = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
        this.isOpen = false;
        this.workingHours = "10:00 - 22:00";
    }

    public void open() {
        isOpen = true;
        System.out.println("Пиццерия '" + name + "' открыта!");
    }

    public void close() {
        isOpen = false;
        System.out.println("Пиццерия '" + name + "' закрыта!");
    }

    public void hireEmployee(Employee employee) {
        employees.add(employee);
        System.out.println("Нанят сотрудник: " + employee.getFullName());
    }

    public void fireEmployee(Employee employee) {
        employees.remove(employee);
        employee.setActive(false);
        System.out.println("Уволен сотрудник: " + employee.getFullName());
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
        System.out.println("Заказ #" + order.getId() + " добавлен в историю");
    }

    public double calculateDailyRevenue() {
        double revenue = 0;
        for (Order order : orderHistory) {
            if (order.isPaid()) {
                revenue += order.getFinalPrice();
            }
        }
        return revenue;
    }

    public int getTotalOrders() {
        return orderHistory.size();
    }

    public List<Employee> getActiveEmployees() {
        List<Employee> active = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.isActive()) {
                active.add(emp);
            }
        }
        return active;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }

    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }

    public Kitchen getKitchen() { return kitchen; }
    public void setKitchen(Kitchen kitchen) { this.kitchen = kitchen; }

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }

    public List<Order> getOrderHistory() { return orderHistory; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}
