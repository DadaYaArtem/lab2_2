package com.pizzeria.interfaces;

import com.pizzeria.exceptions.InvalidDiscountException;

/**
 * Интерфейс для применения скидок
 */
public interface Discountable {
    /**
     * Применить скидку в процентах
     */
    void applyDiscount(double percentage) throws InvalidDiscountException;

    /**
     * Получить размер скидки
     */
    double getDiscountAmount();

    /**
     * Проверить, применима ли скидка
     */
    boolean isDiscountApplicable();
}
