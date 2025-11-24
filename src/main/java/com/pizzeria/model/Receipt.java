package com.pizzeria.model;

import com.pizzeria.model.payment.Payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс чека
 */
public class Receipt {
    private String receiptNumber;
    private Order order;
    private Payment payment;
    private LocalDateTime issueTime;

    public Receipt(String receiptNumber, Order order, Payment payment) {
        this.receiptNumber = receiptNumber;
        this.order = order;
        this.payment = payment;
        this.issueTime = LocalDateTime.now();
    }

    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==========================================\n");
        receipt.append("           ЧЕК №").append(receiptNumber).append("\n");
        receipt.append("==========================================\n");
        receipt.append("Дата: ").append(formatDateTime(issueTime)).append("\n");
        receipt.append("Клиент: ").append(order.getCustomer().getFullName()).append("\n");
        receipt.append("------------------------------------------\n");
        receipt.append("Товары:\n");

        for (OrderItem item : order.getItems()) {
            receipt.append(String.format("  %s x%d - %.2f руб.\n",
                item.getProduct().getName(),
                item.getQuantity(),
                item.getTotalPrice()));
        }

        receipt.append("------------------------------------------\n");
        receipt.append(String.format("Сумма: %.2f руб.\n", order.getPrice()));

        if (order.getDiscountPercentage() > 0) {
            receipt.append(String.format("Скидка: %.0f%%\n", order.getDiscountPercentage()));
        }

        double deliveryCost = order.calculateDeliveryCost();
        if (deliveryCost > 0) {
            receipt.append(String.format("Доставка: %.2f руб.\n", deliveryCost));
        }

        receipt.append(String.format("ИТОГО: %.2f руб.\n", order.getFinalPrice()));
        receipt.append("------------------------------------------\n");
        receipt.append("Оплата: ").append(payment.getMethod().getDisplayName()).append("\n");
        receipt.append("==========================================\n");
        receipt.append("     Спасибо за ваш заказ!\n");
        receipt.append("==========================================\n");

        return receipt.toString();
    }

    public void print() {
        System.out.println(generateReceipt());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    // Getters and Setters
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public LocalDateTime getIssueTime() { return issueTime; }
    public void setIssueTime(LocalDateTime issueTime) { this.issueTime = issueTime; }
}
