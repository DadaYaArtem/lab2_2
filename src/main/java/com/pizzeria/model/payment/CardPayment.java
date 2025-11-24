package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;

/**
 * Оплата картой
 */
public class CardPayment extends Payment {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;

    public CardPayment(String transactionId, double amount, String cardNumber)
            throws InvalidPaymentException {
        super(transactionId, amount, PaymentMethod.CARD);
        this.cardNumber = maskCardNumber(cardNumber);
    }

    @Override
    public boolean process() throws InvalidPaymentException {
        if (!validateCard()) {
            throw new InvalidPaymentException(amount, "Некорректные данные карты");
        }

        // Симуляция обработки платежа
        System.out.println("Обработка платежа картой: " + cardNumber);
        isSuccessful = true;
        return true;
    }

    @Override
    public void refund() throws InvalidPaymentException {
        if (!isSuccessful) {
            throw new InvalidPaymentException("Возврат невозможен - платеж не был успешным");
        }
        System.out.println("Возврат " + amount + " руб. на карту " + cardNumber);
        isSuccessful = false;
    }

    private boolean validateCard() {
        // Упрощенная валидация
        return cardNumber != null && cardNumber.length() >= 16;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public boolean verifyPin(String pin) {
        // Упрощенная проверка PIN
        return pin != null && pin.length() == 4;
    }

    // Getters and Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = maskCardNumber(cardNumber); }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}
