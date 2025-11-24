package com.pizzeria.strategy;

import com.pizzeria.model.Order;

/**
 * Премиум стратегия ценообразования (с дополнительными услугами)
 */
public class PremiumPricingStrategy implements PriceCalculationStrategy {
    private double serviceFee;

    public PremiumPricingStrategy(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    @Override
    public double calculatePrice(Order order) {
        double basePrice = order.getPrice();
        double deliveryCost = order.calculateDeliveryCost();
        double total = basePrice + deliveryCost;

        // Добавляем премиум сервисный сбор
        return total + serviceFee;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }
}
