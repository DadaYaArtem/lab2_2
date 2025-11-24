package com.pizzeria.model.products;

import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.interfaces.Payable;

/**
 * Абстрактный класс продукта
 */
public abstract class Product implements Payable {
    protected String name;
    protected double basePrice;
    protected double discountPercentage;
    protected String description;
    protected boolean isAvailable;

    public Product(String name, double basePrice) throws InvalidPriceException {
        if (basePrice < 0) {
            throw new InvalidPriceException(basePrice);
        }
        this.name = name;
        this.basePrice = basePrice;
        this.discountPercentage = 0;
        this.isAvailable = true;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }

    @Override
    public void applyDiscount(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getFinalPrice() {
        return basePrice * (1 - discountPercentage / 100.0);
    }

    public abstract int getPreparationTime();
    public abstract int getCalories();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("%s - %.2f руб.", name, getFinalPrice());
    }
}
