package com.pizzeria.model;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.interfaces.Deliverable;
import com.pizzeria.interfaces.Payable;
import com.pizzeria.model.products.Product;
import com.pizzeria.model.users.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс заказа
 */
public class Order implements Payable, Deliverable {
    private String id;
    private Customer customer;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private Address deliveryAddress;
    private double discountPercentage;
    private boolean isPaid;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
        this.discountPercentage = 0;
        this.isPaid = false;
    }

    public void addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(product, quantity);
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    @Override
    public double getPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public void applyDiscount(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getFinalPrice() {
        double basePrice = getPrice();
        double deliveryCost = calculateDeliveryCost();
        return (basePrice + deliveryCost) * (1 - discountPercentage / 100.0);
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount >= getFinalPrice()) {
            isPaid = true;
            status = OrderStatus.CONFIRMED;
            return true;
        }
        return false;
    }

    @Override
    public void setDeliveryAddress(Address address) throws InvalidDeliveryAddressException {
        if (address == null) {
            throw new InvalidDeliveryAddressException("null", "Адрес не может быть пустым");
        }
        this.deliveryAddress = address;
    }

    @Override
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    @Override
    public int calculateDeliveryTime() {
        if (deliveryAddress == null) {
            return 0; // самовывоз
        }
        // Упрощенный расчет: 30 минут + 5 минут на км
        return 30 + (int)(deliveryAddress.getLatitude() * 5);
    }

    @Override
    public double calculateDeliveryCost() {
        if (deliveryAddress == null) {
            return 0; // самовывоз
        }
        double distance = deliveryAddress.getLatitude(); // упрощенно
        if (distance < 3) return 100;
        if (distance < 5) return 150;
        return 200;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        System.out.println("Статус заказа #" + id + " изменен на: " + newStatus.getDisplayName());
    }

    public int getTotalItems() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public double getDiscountPercentage() { return discountPercentage; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return String.format("Заказ #%s - %s (%.2f руб.)", id, status.getDisplayName(), getFinalPrice());
    }
}
