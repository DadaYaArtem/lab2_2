package com.pizzeria.model.products;

import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс десерта
 */
public class Dessert extends Product {
    private int weight; // в граммах
    private boolean isSweet;
    private boolean containsNuts;

    public Dessert(String name, double basePrice, int weight) throws InvalidPriceException {
        super(name, basePrice);
        this.weight = weight;
        this.isSweet = true;
    }

    @Override
    public int getPreparationTime() {
        return 5; // минуты
    }

    @Override
    public int getCalories() {
        return weight * 3; // примерно 300 калорий на 100г
    }

    @Override
    public boolean processPayment(double amount) throws InvalidPaymentException {
        if (amount < getFinalPrice()) {
            throw new InvalidPaymentException(amount, "Недостаточно средств");
        }
        return true;
    }

    public boolean isSuitableForDiabetics() {
        return !isSweet;
    }

    // Getters and Setters
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public boolean isSweet() { return isSweet; }
    public void setSweet(boolean sweet) { isSweet = sweet; }

    public boolean isContainsNuts() { return containsNuts; }
    public void setContainsNuts(boolean containsNuts) { this.containsNuts = containsNuts; }
}
