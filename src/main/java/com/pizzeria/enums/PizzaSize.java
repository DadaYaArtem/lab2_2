package com.pizzeria.enums;

/**
 * Размеры пиццы
 */
public enum PizzaSize {
    SMALL("Маленькая", 25, 1.0),
    MEDIUM("Средняя", 30, 1.5),
    LARGE("Большая", 35, 2.0),
    EXTRA_LARGE("Очень большая", 40, 2.5);

    private final String displayName;
    private final int diameter;
    private final double priceMultiplier;

    PizzaSize(String displayName, int diameter, double priceMultiplier) {
        this.displayName = displayName;
        this.diameter = diameter;
        this.priceMultiplier = priceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDiameter() {
        return diameter;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
