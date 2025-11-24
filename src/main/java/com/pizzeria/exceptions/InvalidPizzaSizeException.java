package com.pizzeria.exceptions;

/**
 * Исключение для некорректного размера пиццы
 */
public class InvalidPizzaSizeException extends Exception {
    private String providedSize;

    public InvalidPizzaSizeException(String providedSize) {
        super("Некорректный размер пиццы: " + providedSize + ". Допустимые размеры: SMALL, MEDIUM, LARGE, EXTRA_LARGE");
        this.providedSize = providedSize;
    }

    public String getProvidedSize() {
        return providedSize;
    }
}
