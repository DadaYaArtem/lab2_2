package com.pizzeria.factory;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPizzaSizeException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.products.*;

/**
 * Фабрика для создания пицц (Factory Pattern)
 */
public class PizzaFactory {

    public Pizza createPizza(String type, PizzaSize size)
            throws InvalidPriceException, InvalidPizzaSizeException {
        if (size == null) {
            throw new InvalidPizzaSizeException("null");
        }

        switch (type.toLowerCase()) {
            case "margherita":
            case "маргарита":
                return new MargheritaPizza(size);

            case "pepperoni":
            case "пепперони":
                return new PepperoniPizza(size);

            case "veggie":
            case "вегетарианская":
                return new VeggiePizza(size);

            case "meat":
            case "мясная":
                return new MeatLoversPizza(size);

            case "custom":
            case "пользовательская":
                return new CustomPizza(size);

            default:
                throw new IllegalArgumentException("Неизвестный тип пиццы: " + type);
        }
    }

    public Pizza createDefaultPizza() throws InvalidPriceException {
        return new MargheritaPizza(PizzaSize.MEDIUM);
    }

    public Pizza createLargePizza(String type) throws InvalidPriceException, InvalidPizzaSizeException {
        return createPizza(type, PizzaSize.LARGE);
    }
}
