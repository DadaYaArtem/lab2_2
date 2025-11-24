package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс овощей
 */
public class Vegetable extends Ingredient {
    private boolean isOrganic;
    private String season;
    private boolean requiresPreparation;

    public Vegetable(String name, double pricePerUnit) throws InvalidPriceException {
        super(name, pricePerUnit, "грамм");
        this.isVegetarian = true;
        this.calories = 30;
    }

    @Override
    public String getCategory() {
        return "Овощи";
    }

    public String getPreparationMethod() {
        if (name.equalsIgnoreCase("Помидоры")) {
            return "Нарезать кружочками";
        } else if (name.equalsIgnoreCase("Лук")) {
            return "Нарезать кольцами";
        }
        return "Нарезать";
    }

    public boolean isFresh() {
        return !requiresPreparation;
    }

    // Getters and Setters
    public boolean isOrganic() { return isOrganic; }
    public void setOrganic(boolean organic) { isOrganic = organic; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public boolean isRequiresPreparation() { return requiresPreparation; }
    public void setRequiresPreparation(boolean requiresPreparation) {
        this.requiresPreparation = requiresPreparation;
    }
}
