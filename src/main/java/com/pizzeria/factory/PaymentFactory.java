package com.pizzeria.factory;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.payment.*;

/**
 * Фабрика для создания платежей
 */
public class PaymentFactory {

    public Payment createPayment(PaymentMethod method, String transactionId, double amount)
            throws InvalidPaymentException {
        switch (method) {
            case CASH:
                return new CashPayment(transactionId, amount);

            case CARD:
                return new CardPayment(transactionId, amount, "1234567890123456");

            case ONLINE:
                return new OnlinePayment(transactionId, amount, "customer@example.com");

            default:
                throw new IllegalArgumentException("Неподдерживаемый метод оплаты: " + method);
        }
    }

    public CashPayment createCashPayment(double amount) throws InvalidPaymentException {
        String transactionId = "CASH-" + System.currentTimeMillis();
        return new CashPayment(transactionId, amount);
    }

    public CardPayment createCardPayment(double amount, String cardNumber)
            throws InvalidPaymentException {
        String transactionId = "CARD-" + System.currentTimeMillis();
        return new CardPayment(transactionId, amount, cardNumber);
    }

    public OnlinePayment createOnlinePayment(double amount, String email)
            throws InvalidPaymentException {
        String transactionId = "ONLINE-" + System.currentTimeMillis();
        return new OnlinePayment(transactionId, amount, email);
    }
}
