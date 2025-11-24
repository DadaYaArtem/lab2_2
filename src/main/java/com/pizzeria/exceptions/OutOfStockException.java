package com.pizzeria.exceptions;

/**
 * Исключение когда продукт закончился
 */
public class OutOfStockException extends Exception {
    private String productName;

    public OutOfStockException(String productName) {
        super("Продукт '" + productName + "' закончился на складе");
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
