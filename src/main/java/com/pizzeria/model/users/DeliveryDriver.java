package com.pizzeria.model.users;

import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.model.Address;

/**
 * Класс водителя доставки
 */
public class DeliveryDriver extends Employee {
    private String vehicleType;
    private String vehicleNumber;
    private boolean isAvailable;
    private int deliveriesCompleted;

    public DeliveryDriver(String id, String firstName, String lastName, double salary) {
        super(id, firstName, lastName, EmployeeRole.DELIVERY_DRIVER, salary);
        this.isAvailable = true;
        this.deliveriesCompleted = 0;
    }

    @Override
    public String getRole() {
        return "Водитель доставки";
    }

    @Override
    public void performDuty() {
        System.out.println(getFullName() + " доставляет заказы");
    }

    public int calculateDeliveryTime(Address from, Address to) {
        double distance = from.calculateDistance(to);
        return (int)(distance / 0.5); // 30 км/ч средняя скорость
    }

    public void startDelivery() {
        isAvailable = false;
        System.out.println(getFullName() + " начал доставку");
    }

    public void completeDelivery() {
        isAvailable = true;
        deliveriesCompleted++;
        System.out.println(getFullName() + " завершил доставку");
    }

    public double calculateDeliveryBonus() {
        return deliveriesCompleted * 50.0; // 50 руб за доставку
    }

    // Getters and Setters
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getDeliveriesCompleted() { return deliveriesCompleted; }
}
