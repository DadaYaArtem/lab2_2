package com.pizzeria.enums;

/**
 * Методы оплаты
 */
public enum PaymentMethod {
    CASH("Наличные"),
    CARD("Банковская карта"),
    ONLINE("Онлайн оплата"),
    CRYPTO("Криптовалюта");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
