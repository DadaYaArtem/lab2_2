package com.pizzeria.enums;

/**
 * Статусы заказа
 */
public enum OrderStatus {
    PENDING("Ожидает обработки"),
    CONFIRMED("Подтвержден"),
    PREPARING("Готовится"),
    READY("Готов"),
    IN_DELIVERY("В доставке"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменен"),
    COMPLETED("Завершен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
