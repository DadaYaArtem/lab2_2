package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Абстрактный класс ингредиента
 */
public abstract class Ingredient {
    protected String name;
    protected double pricePerUnit;
    protected int quantity;
    protected String unit;
    protected boolean isVegetarian;
    protected int calories;

    public Ingredient(String name, double pricePerUnit, String unit) throws InvalidPriceException {
        if (pricePerUnit < 0) {
            throw new InvalidPriceException(pricePerUnit);
        }
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.quantity = 0;
    }

    public abstract String getCategory();

    public boolean checkAvailability(int requiredQuantity) {
        return this.quantity >= requiredQuantity;
    }

    public void addStock(int amount) {
        this.quantity += amount;
    }

    public void removeStock(int amount) {
        this.quantity -= amount;
    }

    public double calculateCost(int amount) {
        return pricePerUnit * amount;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public boolean isVegetarian() { return isVegetarian; }
    public void setVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    @Override
    public String toString() {
        return String.format("%s (%d %s)", name, quantity, unit);
    }
}
