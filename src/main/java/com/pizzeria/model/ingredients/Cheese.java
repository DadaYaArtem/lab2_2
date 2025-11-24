package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс сыра
 */
public class Cheese extends Ingredient {
    private String cheeseType;
    private double fatContent;
    private boolean isAged;

    public Cheese(String name, double pricePerUnit, String cheeseType) throws InvalidPriceException {
        super(name, pricePerUnit, "грамм");
        this.cheeseType = cheeseType;
        this.isVegetarian = true;
        this.calories = 350;
    }

    @Override
    public String getCategory() {
        return "Сыр";
    }

    public boolean isSuitableForVegans() {
        return false; // сыр не подходит для веганов
    }

    public String getMeltingQuality() {
        if (cheeseType.equalsIgnoreCase("Моцарелла")) {
            return "Отличное";
        } else if (cheeseType.equalsIgnoreCase("Пармезан")) {
            return "Среднее";
        }
        return "Хорошее";
    }

    // Getters and Setters
    public String getCheeseType() { return cheeseType; }
    public void setCheeseType(String cheeseType) { this.cheeseType = cheeseType; }

    public double getFatContent() { return fatContent; }
    public void setFatContent(double fatContent) { this.fatContent = fatContent; }

    public boolean isAged() { return isAged; }
    public void setAged(boolean aged) { isAged = aged; }
}
