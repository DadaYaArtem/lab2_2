package com.pizzeria.service;

import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.model.Address;
import com.pizzeria.model.DeliveryInfo;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.DeliveryDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис доставки
 */
public class DeliveryService {
    private List<DeliveryDriver> drivers;
    private List<DeliveryInfo> activeDeliveries;

    public DeliveryService() {
        this.drivers = new ArrayList<>();
        this.activeDeliveries = new ArrayList<>();
    }

    public void addDriver(DeliveryDriver driver) {
        drivers.add(driver);
        System.out.println("Добавлен водитель: " + driver.getFullName());
    }

    public DeliveryDriver findAvailableDriver() {
        for (DeliveryDriver driver : drivers) {
            if (driver.isAvailable()) {
                return driver;
            }
        }
        return null;
    }

    public DeliveryInfo scheduleDelivery(Order order, DeliveryDriver driver)
            throws InvalidDeliveryAddressException {
        if (order.getDeliveryAddress() == null) {
            throw new InvalidDeliveryAddressException("null", "Адрес доставки не указан");
        }

        DeliveryInfo delivery = new DeliveryInfo(order, driver);
        activeDeliveries.add(delivery);
        driver.startDelivery();

        System.out.println("Запланирована доставка заказа #" + order.getId() +
            " водителем " + driver.getFullName());

        return delivery;
    }

    public void completeDelivery(DeliveryInfo delivery) {
        delivery.complete();
        delivery.getDriver().completeDelivery();
        activeDeliveries.remove(delivery);

        System.out.println("Доставка завершена для заказа #" + delivery.getOrder().getId());
    }

    public boolean validateAddress(Address address) {
        return address != null &&
               address.getStreet() != null &&
               address.getHouseNumber() != null &&
               address.getCity() != null;
    }

    public int getActiveDeliveriesCount() {
        return activeDeliveries.size();
    }
}
