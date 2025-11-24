package com.pizzeria.exceptions;

/**
 * Исключение для некорректной оплаты
 */
public class InvalidPaymentException extends Exception {
    private double amount;
    private String reason;

    public InvalidPaymentException(String reason) {
        super("Ошибка оплаты: " + reason);
        this.reason = reason;
    }

    public InvalidPaymentException(double amount, String reason) {
        super(String.format("Ошибка оплаты на сумму %.2f: %s", amount, reason));
        this.amount = amount;
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }
}
