package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс теста
 */
public class Dough extends Ingredient {
    private String doughType;
    private int thickness;
    private boolean isGlutenFree;

    public Dough(String name, double pricePerUnit, String doughType) throws InvalidPriceException {
        super(name, pricePerUnit, "грамм");
        this.doughType = doughType;
        this.isVegetarian = true;
        this.calories = 270;
    }

    @Override
    public String getCategory() {
        return "Тесто";
    }

    public int getPreparationTime() {
        if (doughType.equalsIgnoreCase("Дрожжевое")) {
            return 60; // минут для подъема
        }
        return 30;
    }

    public boolean isCrispy() {
        return thickness < 3;
    }

    // Getters and Setters
    public String getDoughType() { return doughType; }
    public void setDoughType(String doughType) { this.doughType = doughType; }

    public int getThickness() { return thickness; }
    public void setThickness(int thickness) { this.thickness = thickness; }

    public boolean isGlutenFree() { return isGlutenFree; }
    public void setGlutenFree(boolean glutenFree) { isGlutenFree = glutenFree; }
}
