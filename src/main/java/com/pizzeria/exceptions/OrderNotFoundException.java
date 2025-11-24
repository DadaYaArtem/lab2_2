package com.pizzeria.exceptions;

/**
 * Исключение когда заказ не найден
 */
public class OrderNotFoundException extends Exception {
    private String orderId;

    public OrderNotFoundException(String orderId) {
        super("Заказ с ID " + orderId + " не найден");
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
