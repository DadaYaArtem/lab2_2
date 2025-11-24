package com.pizzeria.factory;

import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.*;

/**
 * Фабрика для создания ингредиентов (Abstract Factory Pattern)
 */
public class IngredientFactory {

    public Cheese createCheese(String type) throws InvalidPriceException {
        Cheese cheese = new Cheese(type, 50.0, type);
        cheese.setQuantity(100);
        return cheese;
    }

    public Meat createMeat(String type) throws InvalidPriceException {
        Meat meat = new Meat(type, 120.0, type);
        meat.setQuantity(80);
        return meat;
    }

    public Vegetable createVegetable(String name) throws InvalidPriceException {
        Vegetable vegetable = new Vegetable(name, 30.0);
        vegetable.setQuantity(150);
        return vegetable;
    }

    public Sauce createSauce(String type) throws InvalidPriceException {
        Sauce sauce = new Sauce(type, 40.0, type);
        sauce.setQuantity(200);
        return sauce;
    }

    public Dough createDough(String type) throws InvalidPriceException {
        Dough dough = new Dough(type, 25.0, type);
        dough.setQuantity(120);
        return dough;
    }

    public Ingredient createIngredient(String category, String name) throws InvalidPriceException {
        switch (category.toLowerCase()) {
            case "cheese":
            case "сыр":
                return createCheese(name);
            case "meat":
            case "мясо":
                return createMeat(name);
            case "vegetable":
            case "овощи":
                return createVegetable(name);
            case "sauce":
            case "соус":
                return createSauce(name);
            case "dough":
            case "тесто":
                return createDough(name);
            default:
                throw new IllegalArgumentException("Неизвестная категория: " + category);
        }
    }
}
