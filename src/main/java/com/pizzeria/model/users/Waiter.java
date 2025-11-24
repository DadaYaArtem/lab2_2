package com.pizzeria.model.users;

import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс официанта
 */
public class Waiter extends Employee {
    private List<Order> currentOrders;
    private double tips;

    public Waiter(String id, String firstName, String lastName, double salary) {
        super(id, firstName, lastName, EmployeeRole.WAITER, salary);
        this.currentOrders = new ArrayList<>();
        this.tips = 0;
    }

    @Override
    public String getRole() {
        return "Официант";
    }

    @Override
    public void performDuty() {
        System.out.println(getFullName() + " обслуживает столики");
    }

    public void takeOrder(Order order) {
        currentOrders.add(order);
        System.out.println(getFullName() + " принял заказ #" + order.getId());
    }

    public void serveOrder(Order order) {
        currentOrders.remove(order);
        System.out.println(getFullName() + " подал заказ #" + order.getId());
    }

    public void receiveTip(double amount) {
        tips += amount;
    }

    public double getTotalEarnings() {
        return salary + tips;
    }

    public boolean isBusy() {
        return currentOrders.size() > 5;
    }

    // Getters and Setters
    public List<Order> getCurrentOrders() { return currentOrders; }

    public double getTips() { return tips; }
    public void setTips(double tips) { this.tips = tips; }
}
