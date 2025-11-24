package com.pizzeria.util;

import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;

/**
 * Утилитарный класс для валидации заказов
 */
public class OrderValidator {

    public static boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }

        if (order.getCustomer() == null) {
            System.out.println("Ошибка: заказ должен иметь клиента");
            return false;
        }

        if (order.getItems().isEmpty()) {
            System.out.println("Ошибка: заказ должен содержать хотя бы один товар");
            return false;
        }

        return validateOrderItems(order);
    }

    private static boolean validateOrderItems(Order order) {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null) {
                System.out.println("Ошибка: элемент заказа содержит null продукт");
                return false;
            }

            if (item.getQuantity() <= 0) {
                System.out.println("Ошибка: количество должно быть положительным");
                return false;
            }

            if (!item.getProduct().isAvailable()) {
                System.out.println("Ошибка: продукт " + item.getProduct().getName() + " недоступен");
                return false;
            }
        }

        return true;
    }

    public static boolean validateMinimumOrderAmount(Order order, double minimumAmount) {
        return order.getPrice() >= minimumAmount;
    }

    public static boolean validateDeliveryAddress(Order order) {
        return order.getDeliveryAddress() != null;
    }
}
