package com.pizzeria.exceptions;

/**
 * Исключение когда заказ с таким ID уже существует
 */
public class DuplicateOrderException extends Exception {
    private String orderId;

    public DuplicateOrderException(String orderId) {
        super("Заказ с ID " + orderId + " уже существует");
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
