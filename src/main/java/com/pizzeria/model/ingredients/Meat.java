package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс мяса
 */
public class Meat extends Ingredient {
    private String meatType;
    private boolean isProcessed;
    private String origin;

    public Meat(String name, double pricePerUnit, String meatType) throws InvalidPriceException {
        super(name, pricePerUnit, "грамм");
        this.meatType = meatType;
        this.isVegetarian = false;
        this.calories = 250;
    }

    @Override
    public String getCategory() {
        return "Мясо";
    }

    public boolean requiresCooking() {
        return !isProcessed;
    }

    public int getCookingTime() {
        if (meatType.equalsIgnoreCase("Курица")) {
            return 15;
        } else if (meatType.equalsIgnoreCase("Говядина")) {
            return 20;
        }
        return 10;
    }

    // Getters and Setters
    public String getMeatType() { return meatType; }
    public void setMeatType(String meatType) { this.meatType = meatType; }

    public boolean isProcessed() { return isProcessed; }
    public void setProcessed(boolean processed) { isProcessed = processed; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
}
