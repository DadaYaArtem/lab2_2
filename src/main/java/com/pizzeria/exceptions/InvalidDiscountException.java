package com.pizzeria.exceptions;

/**
 * Исключение для некорректной скидки
 */
public class InvalidDiscountException extends Exception {
    private double discountPercentage;

    public InvalidDiscountException(double discountPercentage) {
        super(String.format("Некорректная скидка: %.2f%%. Скидка должна быть в диапазоне 0-100%%", discountPercentage));
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
