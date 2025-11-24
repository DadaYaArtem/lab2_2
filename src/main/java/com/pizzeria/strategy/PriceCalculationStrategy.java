package com.pizzeria.strategy;

import com.pizzeria.model.Order;

/**
 * Интерфейс стратегии расчета цены (Strategy Pattern)
 */
public interface PriceCalculationStrategy {
    double calculatePrice(Order order);
}
