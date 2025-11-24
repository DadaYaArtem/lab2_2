package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Пицца Пепперони
 */
public class PepperoniPizza extends Pizza {

    public PepperoniPizza(PizzaSize size) throws InvalidPriceException {
        super("Пепперони", 400.0, size);
        this.description = "Пицца с пепперони, моцареллой и томатным соусом";
        this.cookingTime = 20;
    }

    @Override
    public int getCalories() {
        switch (size) {
            case SMALL: return 1000;
            case MEDIUM: return 1500;
            case LARGE: return 2000;
            case EXTRA_LARGE: return 2500;
            default: return 1500;
        }
    }

    public boolean isSpicy() {
        return true;
    }

    public int getSpicyLevel() {
        return 2; // из 5
    }
}
