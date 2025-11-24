package com.pizzeria.exceptions;

/**
 * Исключение когда клиент не найден
 */
public class CustomerNotFoundException extends Exception {
    private String customerId;

    public CustomerNotFoundException(String customerId) {
        super("Клиент с ID " + customerId + " не найден");
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
