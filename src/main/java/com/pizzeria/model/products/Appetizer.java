package com.pizzeria.model.products;

import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс закуски
 */
public class Appetizer extends Product {
    private boolean isHot;
    private int servingSize;

    public Appetizer(String name, double basePrice) throws InvalidPriceException {
        super(name, basePrice);
        this.isHot = false;
        this.servingSize = 100;
    }

    @Override
    public int getPreparationTime() {
        return isHot ? 10 : 3;
    }

    @Override
    public int getCalories() {
        return servingSize * 2;
    }

    @Override
    public boolean processPayment(double amount) throws InvalidPaymentException {
        if (amount < getFinalPrice()) {
            throw new InvalidPaymentException(amount, "Недостаточно средств");
        }
        return true;
    }

    public void heat() {
        this.isHot = true;
    }

    // Getters and Setters
    public boolean isHot() { return isHot; }
    public void setHot(boolean hot) { isHot = hot; }

    public int getServingSize() { return servingSize; }
    public void setServingSize(int servingSize) { this.servingSize = servingSize; }
}
