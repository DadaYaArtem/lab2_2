package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;

/**
 * Оплата наличными
 */
public class CashPayment extends Payment {
    private double amountReceived;
    private double change;

    public CashPayment(String transactionId, double amount) throws InvalidPaymentException {
        super(transactionId, amount, PaymentMethod.CASH);
    }

    @Override
    public boolean process() throws InvalidPaymentException {
        if (amountReceived < amount) {
            throw new InvalidPaymentException(amountReceived,
                "Получено недостаточно средств. Требуется: " + amount);
        }
        change = amountReceived - amount;
        isSuccessful = true;
        System.out.println("Оплата наличными успешна. Сдача: " + change + " руб.");
        return true;
    }

    @Override
    public void refund() throws InvalidPaymentException {
        if (!isSuccessful) {
            throw new InvalidPaymentException("Возврат невозможен - платеж не был успешным");
        }
        System.out.println("Возврат " + amount + " руб. наличными");
        isSuccessful = false;
    }

    public double calculateChange(double received) {
        return received - amount;
    }

    // Getters and Setters
    public double getAmountReceived() { return amountReceived; }
    public void setAmountReceived(double amountReceived) { this.amountReceived = amountReceived; }

    public double getChange() { return change; }
}
