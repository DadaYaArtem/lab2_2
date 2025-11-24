package com.pizzeria.model;

import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.exceptions.OutOfStockException;
import com.pizzeria.model.ingredients.Ingredient;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс инвентаря
 */
public class Inventory {
    private Map<String, Ingredient> ingredients;
    private int lowStockThreshold;

    public Inventory() {
        this.ingredients = new HashMap<>();
        this.lowStockThreshold = 10;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.put(ingredient.getName(), ingredient);
        System.out.println("Добавлен ингредиент: " + ingredient.getName());
    }

    public void removeIngredient(String ingredientName) {
        ingredients.remove(ingredientName);
        System.out.println("Удален ингредиент: " + ingredientName);
    }

    public boolean checkAvailability(String ingredientName, int quantity) {
        Ingredient ingredient = ingredients.get(ingredientName);
        return ingredient != null && ingredient.checkAvailability(quantity);
    }

    public void useIngredient(String ingredientName, int quantity) throws InsufficientIngredientsException {
        Ingredient ingredient = ingredients.get(ingredientName);
        if (ingredient == null || !ingredient.checkAvailability(quantity)) {
            throw new InsufficientIngredientsException(ingredientName, quantity,
                ingredient != null ? ingredient.getQuantity() : 0);
        }
        ingredient.removeStock(quantity);
        checkLowStock(ingredient);
    }

    public void restockIngredient(String ingredientName, int quantity) throws OutOfStockException {
        Ingredient ingredient = ingredients.get(ingredientName);
        if (ingredient == null) {
            throw new OutOfStockException(ingredientName);
        }
        ingredient.addStock(quantity);
        System.out.println("Пополнен запас: " + ingredientName + " (+"+quantity+")");
    }

    private void checkLowStock(Ingredient ingredient) {
        if (ingredient.getQuantity() < lowStockThreshold) {
            System.out.println("ВНИМАНИЕ: Низкий запас ингредиента " + ingredient.getName());
        }
    }

    public Map<String, Integer> getLowStockItems() {
        Map<String, Integer> lowStock = new HashMap<>();
        for (Ingredient ingredient : ingredients.values()) {
            if (ingredient.getQuantity() < lowStockThreshold) {
                lowStock.put(ingredient.getName(), ingredient.getQuantity());
            }
        }
        return lowStock;
    }

    public double calculateTotalInventoryValue() {
        double total = 0;
        for (Ingredient ingredient : ingredients.values()) {
            total += ingredient.getPricePerUnit() * ingredient.getQuantity();
        }
        return total;
    }

    // Getters and Setters
    public Map<String, Ingredient> getIngredients() { return ingredients; }

    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
}
