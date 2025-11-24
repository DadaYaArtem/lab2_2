package com.pizzeria.model;

import java.time.LocalDate;

/**
 * Класс акции/промоакции
 */
public class Promotion {
    private String name;
    private String description;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public Promotion(String name, double discountPercentage) {
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.startDate = LocalDate.now();
        this.endDate = startDate.plusWeeks(1);
        this.isActive = true;
    }

    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return isActive &&
               !now.isBefore(startDate) &&
               !now.isAfter(endDate);
    }

    public void activate() {
        this.isActive = true;
        System.out.println("Акция '" + name + "' активирована");
    }

    public void deactivate() {
        this.isActive = false;
        System.out.println("Акция '" + name + "' деактивирована");
    }

    public void extend(int days) {
        this.endDate = this.endDate.plusDays(days);
        System.out.println("Срок акции '" + name + "' продлен на " + days + " дней");
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
