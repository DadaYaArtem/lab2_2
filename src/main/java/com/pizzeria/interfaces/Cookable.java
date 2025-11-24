package com.pizzeria.interfaces;

import com.pizzeria.exceptions.InsufficientIngredientsException;

/**
 * Интерфейс для приготовления блюд
 */
public interface Cookable {
    /**
     * Приготовить блюдо
     * @return время приготовления в минутах
     */
    int cook() throws InsufficientIngredientsException;

    /**
     * Получить статус готовности
     */
    boolean isReady();

    /**
     * Получить описание блюда
     */
    String getDescription();
}
