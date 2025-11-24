package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Пицца Маргарита
 */
public class MargheritaPizza extends Pizza {

    public MargheritaPizza(PizzaSize size) throws InvalidPriceException {
        super("Маргарита", 300.0, size);
        this.description = "Классическая пицца с томатным соусом, моцареллой и базиликом";
        this.cookingTime = 18;
    }

    @Override
    public int getCalories() {
        switch (size) {
            case SMALL: return 800;
            case MEDIUM: return 1200;
            case LARGE: return 1600;
            case EXTRA_LARGE: return 2000;
            default: return 1200;
        }
    }

    public boolean isVegetarian() {
        return true;
    }

    public String getOrigin() {
        return "Италия, Неаполь";
    }
}
