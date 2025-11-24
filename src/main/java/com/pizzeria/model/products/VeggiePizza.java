package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Вегетарианская пицца
 */
public class VeggiePizza extends Pizza {

    public VeggiePizza(PizzaSize size) throws InvalidPriceException {
        super("Вегетарианская", 350.0, size);
        this.description = "Пицца с овощами: помидоры, перец, лук, грибы";
        this.cookingTime = 19;
    }

    @Override
    public int getCalories() {
        switch (size) {
            case SMALL: return 700;
            case MEDIUM: return 1000;
            case LARGE: return 1400;
            case EXTRA_LARGE: return 1800;
            default: return 1000;
        }
    }

    public boolean isVegan() {
        return false; // содержит сыр
    }

    public boolean isHealthy() {
        return true;
    }
}
