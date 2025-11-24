package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDiscountException;
import com.pizzeria.interfaces.Discountable;

import java.time.LocalDate;

/**
 * Класс карты лояльности
 */
public class LoyaltyCard implements Discountable {
    private String cardNumber;
    private int points;
    private String tier; // Bronze, Silver, Gold, Platinum
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public LoyaltyCard(String cardNumber) {
        this.cardNumber = cardNumber;
        this.points = 0;
        this.tier = "Bronze";
        this.issueDate = LocalDate.now();
        this.expiryDate = issueDate.plusYears(1);
    }

    public void addPoints(int points) {
        this.points += points;
        updateTier();
    }

    public boolean redeemPoints(int points) {
        if (this.points >= points) {
            this.points -= points;
            return true;
        }
        return false;
    }

    private void updateTier() {
        if (points >= 1000) {
            tier = "Platinum";
        } else if (points >= 500) {
            tier = "Gold";
        } else if (points >= 200) {
            tier = "Silver";
        } else {
            tier = "Bronze";
        }
    }

    @Override
    public void applyDiscount(double percentage) throws InvalidDiscountException {
        if (percentage < 0 || percentage > 100) {
            throw new InvalidDiscountException(percentage);
        }
        // Скидка применяется через другие механизмы
    }

    @Override
    public double getDiscountAmount() {
        switch (tier) {
            case "Platinum": return 15.0;
            case "Gold": return 10.0;
            case "Silver": return 5.0;
            default: return 2.0;
        }
    }

    @Override
    public boolean isDiscountApplicable() {
        return LocalDate.now().isBefore(expiryDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public void renewCard() {
        this.expiryDate = LocalDate.now().plusYears(1);
    }

    // Getters and Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public String getTier() { return tier; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
