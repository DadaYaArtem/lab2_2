package com.pizzeria.enums;

/**
 * Роли сотрудников
 */
public enum EmployeeRole {
    CHEF("Повар"),
    WAITER("Официант"),
    DELIVERY_DRIVER("Водитель доставки"),
    MANAGER("Менеджер"),
    CASHIER("Кассир");

    private final String displayName;

    EmployeeRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
