package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.Ingredient;

/**
 * Пользовательская пицца
 */
public class CustomPizza extends Pizza {
    private double extraIngredientsPrice;

    public CustomPizza(PizzaSize size) throws InvalidPriceException {
        super("Пользовательская пицца", 250.0, size);
        this.description = "Создайте свою уникальную пиццу";
        this.cookingTime = 22;
        this.extraIngredientsPrice = 0;
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        super.addIngredient(ingredient);
        extraIngredientsPrice += ingredient.getPricePerUnit() * 0.01; // добавляем стоимость ингредиента
    }

    @Override
    public double getPrice() {
        return (basePrice + extraIngredientsPrice) * size.getPriceMultiplier();
    }

    @Override
    public int getCalories() {
        int totalCalories = 0;
        for (Ingredient ingredient : ingredients) {
            totalCalories += ingredient.getCalories();
        }
        return totalCalories;
    }

    public int getIngredientsCount() {
        return ingredients.size();
    }

    public double getExtraIngredientsPrice() {
        return extraIngredientsPrice;
    }
}
