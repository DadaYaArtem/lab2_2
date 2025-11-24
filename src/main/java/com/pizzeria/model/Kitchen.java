package com.pizzeria.model;

import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.model.products.Pizza;
import com.pizzeria.model.users.Chef;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс кухни
 */
public class Kitchen {
    private List<Chef> chefs;
    private List<Order> currentOrders;
    private Inventory inventory;
    private int maxCapacity;

    public Kitchen(Inventory inventory) {
        this.inventory = inventory;
        this.chefs = new ArrayList<>();
        this.currentOrders = new ArrayList<>();
        this.maxCapacity = 10;
    }

    public void addChef(Chef chef) {
        chefs.add(chef);
        System.out.println("Повар добавлен на кухню: " + chef.getFullName());
    }

    public void prepareOrder(Order order) throws InsufficientIngredientsException {
        if (currentOrders.size() >= maxCapacity) {
            System.out.println("Кухня переполнена! Ожидайте...");
            return;
        }

        currentOrders.add(order);
        System.out.println("Заказ #" + order.getId() + " принят на кухню");

        Chef availableChef = findAvailableChef();
        if (availableChef != null) {
            cookOrder(order, availableChef);
        }
    }

    private Chef findAvailableChef() {
        return chefs.isEmpty() ? null : chefs.get(0);
    }

    private void cookOrder(Order order, Chef chef) throws InsufficientIngredientsException {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() instanceof Pizza) {
                Pizza pizza = (Pizza) item.getProduct();
                chef.cookPizza(pizza);
            }
        }
        completeOrder(order);
    }

    public void completeOrder(Order order) {
        currentOrders.remove(order);
        System.out.println("Заказ #" + order.getId() + " готов!");
    }

    public boolean isBusy() {
        return currentOrders.size() >= maxCapacity;
    }

    public int getActiveOrdersCount() {
        return currentOrders.size();
    }

    // Getters and Setters
    public List<Chef> getChefs() { return chefs; }
    public void setChefs(List<Chef> chefs) { this.chefs = chefs; }

    public List<Order> getCurrentOrders() { return currentOrders; }

    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
}
