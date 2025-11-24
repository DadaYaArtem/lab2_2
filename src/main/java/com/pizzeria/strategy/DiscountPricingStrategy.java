package com.pizzeria.strategy;

import com.pizzeria.model.Order;

/**
 * Стратегия ценообразования со скидкой
 */
public class DiscountPricingStrategy implements PriceCalculationStrategy {
    private double discountPercentage;

    public DiscountPricingStrategy(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculatePrice(Order order) {
        double basePrice = order.getPrice();
        double deliveryCost = order.calculateDeliveryCost();
        double total = basePrice + deliveryCost;

        return total * (1 - discountPercentage / 100.0);
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
