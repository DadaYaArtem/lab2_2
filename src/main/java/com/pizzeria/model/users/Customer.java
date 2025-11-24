package com.pizzeria.model.users;

import com.pizzeria.interfaces.Notifiable;
import com.pizzeria.model.LoyaltyCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс клиента
 */
public class Customer extends Person implements Notifiable {
    private LoyaltyCard loyaltyCard;
    private List<String> orderHistory;
    private boolean notificationEnabled;
    private String preferredContact;

    public Customer(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
        this.orderHistory = new ArrayList<>();
        this.notificationEnabled = true;
        this.preferredContact = "email";
    }

    @Override
    public String getRole() {
        return "Клиент";
    }

    @Override
    public void sendNotification(String message) {
        if (notificationEnabled) {
            System.out.println("Отправка уведомления клиенту " + getFullName() + ": " + message);
        }
    }

    @Override
    public String getContactInfo() {
        if ("phone".equals(preferredContact) && phoneNumber != null) {
            return phoneNumber.getFullNumber();
        }
        return email != null ? email.getFullEmail() : "";
    }

    @Override
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void addToOrderHistory(String orderId) {
        orderHistory.add(orderId);
    }

    public int getTotalOrders() {
        return orderHistory.size();
    }

    public boolean isVIP() {
        return orderHistory.size() > 10;
    }

    // Getters and Setters
    public LoyaltyCard getLoyaltyCard() { return loyaltyCard; }
    public void setLoyaltyCard(LoyaltyCard loyaltyCard) { this.loyaltyCard = loyaltyCard; }

    public List<String> getOrderHistory() { return orderHistory; }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public String getPreferredContact() { return preferredContact; }
    public void setPreferredContact(String preferredContact) {
        this.preferredContact = preferredContact;
    }
}
