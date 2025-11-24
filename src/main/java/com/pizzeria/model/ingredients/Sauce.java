package com.pizzeria.model.ingredients;

import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс соуса
 */
public class Sauce extends Ingredient {
    private String sauceType;
    private int spicinessLevel;
    private boolean isHomemade;

    public Sauce(String name, double pricePerUnit, String sauceType) throws InvalidPriceException {
        super(name, pricePerUnit, "мл");
        this.sauceType = sauceType;
        this.isVegetarian = true;
        this.calories = 50;
    }

    @Override
    public String getCategory() {
        return "Соус";
    }

    public String getFlavorProfile() {
        if (sauceType.equalsIgnoreCase("Томатный")) {
            return "Кисло-сладкий";
        } else if (sauceType.equalsIgnoreCase("Сливочный")) {
            return "Нежный";
        }
        return "Пикантный";
    }

    public boolean isSpicy() {
        return spicinessLevel > 3;
    }

    // Getters and Setters
    public String getSauceType() { return sauceType; }
    public void setSauceType(String sauceType) { this.sauceType = sauceType; }

    public int getSpicinessLevel() { return spicinessLevel; }
    public void setSpicinessLevel(int spicinessLevel) { this.spicinessLevel = spicinessLevel; }

    public boolean isHomemade() { return isHomemade; }
    public void setHomemade(boolean homemade) { isHomemade = homemade; }
}
