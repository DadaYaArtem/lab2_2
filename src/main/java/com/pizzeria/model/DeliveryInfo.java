package com.pizzeria.model;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.model.users.DeliveryDriver;

import java.time.LocalDateTime;

/**
 * Класс информации о доставке
 */
public class DeliveryInfo {
    private Order order;
    private DeliveryDriver driver;
    private LocalDateTime dispatchTime;
    private LocalDateTime deliveryTime;
    private int estimatedTime;
    private String trackingNumber;

    public DeliveryInfo(Order order, DeliveryDriver driver) {
        this.order = order;
        this.driver = driver;
        this.dispatchTime = LocalDateTime.now();
        this.estimatedTime = order.calculateDeliveryTime();
        this.trackingNumber = "TRK-" + System.currentTimeMillis();
    }

    public void complete() {
        this.deliveryTime = LocalDateTime.now();
        order.updateStatus(OrderStatus.DELIVERED);
    }

    public int getActualDeliveryTime() {
        if (deliveryTime == null) {
            return 0;
        }
        return (int) java.time.Duration.between(dispatchTime, deliveryTime).toMinutes();
    }

    public boolean isOnTime() {
        return getActualDeliveryTime() <= estimatedTime;
    }

    // Getters and Setters
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public DeliveryDriver getDriver() { return driver; }
    public void setDriver(DeliveryDriver driver) { this.driver = driver; }

    public LocalDateTime getDispatchTime() { return dispatchTime; }
    public void setDispatchTime(LocalDateTime dispatchTime) { this.dispatchTime = dispatchTime; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}
