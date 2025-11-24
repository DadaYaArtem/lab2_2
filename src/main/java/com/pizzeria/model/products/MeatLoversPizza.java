package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Мясная пицца
 */
public class MeatLoversPizza extends Pizza {

    public MeatLoversPizza(PizzaSize size) throws InvalidPriceException {
        super("Мясная", 500.0, size);
        this.description = "Пицца с говядиной, курицей, беконом и ветчиной";
        this.cookingTime = 25;
    }

    @Override
    public int getCalories() {
        switch (size) {
            case SMALL: return 1200;
            case MEDIUM: return 1800;
            case LARGE: return 2400;
            case EXTRA_LARGE: return 3000;
            default: return 1800;
        }
    }

    public int getProteinContent() {
        return 45; // граммы на 100г
    }

    public boolean isHighCalorie() {
        return true;
    }
}
