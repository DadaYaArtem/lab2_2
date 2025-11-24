package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.interfaces.Cookable;
import com.pizzeria.model.ingredients.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс пиццы
 */
public abstract class Pizza extends Product implements Cookable {
    protected PizzaSize size;
    protected List<Ingredient> ingredients;
    protected boolean isReady;
    protected int cookingTime;

    public Pizza(String name, double basePrice, PizzaSize size) throws InvalidPriceException {
        super(name, basePrice);
        this.size = size;
        this.ingredients = new ArrayList<>();
        this.isReady = false;
        this.cookingTime = 20;
    }

    @Override
    public double getPrice() {
        return basePrice * size.getPriceMultiplier();
    }

    @Override
    public int cook() throws InsufficientIngredientsException {
        // Проверка наличия всех ингредиентов
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.checkAvailability(1)) {
                throw new InsufficientIngredientsException(
                    ingredient.getName(), 1, ingredient.getQuantity());
            }
        }

        // "Приготовление" пиццы
        for (Ingredient ingredient : ingredients) {
            ingredient.removeStock(1);
        }

        isReady = true;
        return cookingTime;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder(description);
        desc.append(" Ингредиенты: ");
        for (Ingredient ing : ingredients) {
            desc.append(ing.getName()).append(", ");
        }
        return desc.toString();
    }

    @Override
    public int getPreparationTime() {
        return cookingTime;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public boolean processPayment(double amount) {
        return amount >= getFinalPrice();
    }

    // Getters and Setters
    public PizzaSize getSize() { return size; }
    public void setSize(PizzaSize size) { this.size = size; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }
}
