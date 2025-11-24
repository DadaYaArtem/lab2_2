package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;

/**
 * Онлайн оплата
 */
public class OnlinePayment extends Payment {
    private String email;
    private String paymentGateway;
    private String confirmationCode;

    public OnlinePayment(String transactionId, double amount, String email)
            throws InvalidPaymentException {
        super(transactionId, amount, PaymentMethod.ONLINE);
        this.email = email;
        this.paymentGateway = "PayPal"; // по умолчанию
    }

    @Override
    public boolean process() throws InvalidPaymentException {
        if (email == null || !email.contains("@")) {
            throw new InvalidPaymentException(amount, "Некорректный email");
        }

        // Симуляция онлайн платежа
        confirmationCode = generateConfirmationCode();
        System.out.println("Онлайн платеж обработан через " + paymentGateway);
        System.out.println("Код подтверждения: " + confirmationCode);
        isSuccessful = true;
        return true;
    }

    @Override
    public void refund() throws InvalidPaymentException {
        if (!isSuccessful) {
            throw new InvalidPaymentException("Возврат невозможен - платеж не был успешным");
        }
        System.out.println("Возврат " + amount + " руб. на " + email);
        isSuccessful = false;
    }

    private String generateConfirmationCode() {
        return "CONF-" + System.currentTimeMillis();
    }

    public boolean verifyConfirmationCode(String code) {
        return confirmationCode != null && confirmationCode.equals(code);
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPaymentGateway() { return paymentGateway; }
    public void setPaymentGateway(String paymentGateway) { this.paymentGateway = paymentGateway; }

    public String getConfirmationCode() { return confirmationCode; }
}
