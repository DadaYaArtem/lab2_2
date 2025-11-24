package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDiscountException;
import com.pizzeria.interfaces.Discountable;

import java.time.LocalDate;

/**
 * Класс скидки
 */
public class Discount implements Discountable {
    private String code;
    private double percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private int usageLimit;
    private int timesUsed;

    public Discount(String code, double percentage) throws InvalidDiscountException {
        if (percentage < 0 || percentage > 100) {
            throw new InvalidDiscountException(percentage);
        }
        this.code = code;
        this.percentage = percentage;
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusMonths(1);
        this.isActive = true;
        this.usageLimit = 100;
        this.timesUsed = 0;
    }

    @Override
    public void applyDiscount(double percentage) throws InvalidDiscountException {
        if (percentage < 0 || percentage > 100) {
            throw new InvalidDiscountException(percentage);
        }
        this.percentage = percentage;
    }

    @Override
    public double getDiscountAmount() {
        return percentage;
    }

    @Override
    public boolean isDiscountApplicable() {
        LocalDate now = LocalDate.now();
        return isActive &&
               !now.isBefore(startDate) &&
               !now.isAfter(endDate) &&
               timesUsed < usageLimit;
    }

    public void use() {
        if (isDiscountApplicable()) {
            timesUsed++;
            System.out.println("Скидка '" + code + "' применена (" + timesUsed + "/" + usageLimit + ")");
        }
    }

    public boolean validateCode(String inputCode) {
        return this.code.equalsIgnoreCase(inputCode);
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getUsageLimit() { return usageLimit; }
    public void setUsageLimit(int usageLimit) { this.usageLimit = usageLimit; }

    public int getTimesUsed() { return timesUsed; }
}
