package com.pizzeria.interfaces;

import com.pizzeria.exceptions.InvalidPaymentException;

/**
 * Интерфейс для оплачиваемых объектов
 */
public interface Payable {
    /**
     * Получить цену
     */
    double getPrice();

    /**
     * Применить скидку
     */
    void applyDiscount(double discountPercentage);

    /**
     * Получить финальную цену после скидки
     */
    double getFinalPrice();

    /**
     * Обработать платеж
     */
    boolean processPayment(double amount) throws InvalidPaymentException;
}
