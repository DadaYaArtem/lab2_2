package com.pizzeria.strategy;

import com.pizzeria.model.Order;

/**
 * Стандартная стратегия ценообразования
 */
public class StandardPricingStrategy implements PriceCalculationStrategy {

    @Override
    public double calculatePrice(Order order) {
        double basePrice = order.getPrice();
        double deliveryCost = order.calculateDeliveryCost();
        return basePrice + deliveryCost;
    }
}
