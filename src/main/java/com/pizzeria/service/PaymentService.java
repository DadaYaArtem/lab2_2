package com.pizzeria.service;

import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.Order;
import com.pizzeria.model.Receipt;
import com.pizzeria.model.payment.Payment;

/**
 * Сервис обработки платежей
 */
public class PaymentService {
    private int receiptCounter;

    public PaymentService() {
        this.receiptCounter = 1;
    }

    public Receipt processPayment(Order order, Payment payment) throws InvalidPaymentException {
        // Проверка суммы
        if (payment.getAmount() < order.getFinalPrice()) {
            throw new InvalidPaymentException(payment.getAmount(),
                "Недостаточная сумма для оплаты заказа");
        }

        // Обработка платежа
        boolean success = payment.process();

        if (success) {
            order.processPayment(payment.getAmount());
            System.out.println("Платеж успешно обработан");

            // Генерация чека
            String receiptNumber = "RCP-" + (receiptCounter++);
            Receipt receipt = new Receipt(receiptNumber, order, payment);
            receipt.print();
            return receipt;
        } else {
            throw new InvalidPaymentException("Ошибка обработки платежа");
        }
    }

    public void refundPayment(Payment payment) throws InvalidPaymentException {
        if (!payment.isSuccessful()) {
            throw new InvalidPaymentException("Невозможно вернуть неуспешный платеж");
        }

        payment.refund();
        System.out.println("Возврат платежа выполнен успешно");
    }

    public double calculateTax(double amount) {
        return amount * 0.13; // 13% НДС
    }

    public double calculateServiceFee(double amount) {
        return amount * 0.05; // 5% сервисный сбор
    }
}
