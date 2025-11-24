package com.pizzeria.util;

import com.pizzeria.model.Order;
import com.pizzeria.model.products.Product;

/**
 * Утилитарный класс для расчета цен
 */
public class PriceCalculator {

    public static double calculateWithTax(double price, double taxRate) {
        return price * (1 + taxRate / 100.0);
    }

    public static double calculateWithDiscount(double price, double discountPercentage) {
        return price * (1 - discountPercentage / 100.0);
    }

    public static double calculateTip(double amount, double tipPercentage) {
        return amount * (tipPercentage / 100.0);
    }

    public static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static double calculateOrderTotal(Order order, double taxRate, double tipPercentage) {
        double basePrice = order.getPrice();
        double withTax = calculateWithTax(basePrice, taxRate);
        double tip = calculateTip(withTax, tipPercentage);
        return roundToTwoDecimals(withTax + tip);
    }

    public static String formatPrice(double price) {
        return String.format("%.2f руб.", price);
    }
}
