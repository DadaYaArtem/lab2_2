package com.pizzeria.exceptions;

/**
 * Исключение выбрасывается когда недостаточно ингредиентов для приготовления блюда
 */
public class InsufficientIngredientsException extends Exception {
    private String ingredientName;
    private int requiredQuantity;
    private int availableQuantity;

    public InsufficientIngredientsException(String ingredientName, int requiredQuantity, int availableQuantity) {
        super(String.format("Недостаточно ингредиента '%s'. Требуется: %d, доступно: %d",
              ingredientName, requiredQuantity, availableQuantity));
        this.ingredientName = ingredientName;
        this.requiredQuantity = requiredQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
