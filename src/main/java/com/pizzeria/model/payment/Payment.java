package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;

import java.time.LocalDateTime;

/**
 * Абстрактный класс платежа
 */
public abstract class Payment {
    protected String transactionId;
    protected double amount;
    protected PaymentMethod method;
    protected LocalDateTime paymentTime;
    protected boolean isSuccessful;

    public Payment(String transactionId, double amount, PaymentMethod method) throws InvalidPaymentException {
        if (amount <= 0) {
            throw new InvalidPaymentException(amount, "Сумма должна быть положительной");
        }
        this.transactionId = transactionId;
        this.amount = amount;
        this.method = method;
        this.paymentTime = LocalDateTime.now();
        this.isSuccessful = false;
    }

    public abstract boolean process() throws InvalidPaymentException;

    public abstract void refund() throws InvalidPaymentException;

    public String getReceipt() {
        return String.format("Платеж #%s: %.2f руб. (%s)",
            transactionId, amount, method.getDisplayName());
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }

    public boolean isSuccessful() { return isSuccessful; }
    protected void setSuccessful(boolean successful) { isSuccessful = successful; }
}
