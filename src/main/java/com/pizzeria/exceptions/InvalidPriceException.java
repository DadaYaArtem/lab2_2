package com.pizzeria.exceptions;

/**
 * Исключение для некорректной цены
 */
public class InvalidPriceException extends Exception {
    private double price;

    public InvalidPriceException(double price) {
        super("Некорректная цена: " + price + ". Цена должна быть положительным числом");
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
