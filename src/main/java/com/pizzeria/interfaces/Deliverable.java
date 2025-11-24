package com.pizzeria.interfaces;

import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.model.Address;

/**
 * Интерфейс для доставляемых объектов
 */
public interface Deliverable {
    /**
     * Установить адрес доставки
     */
    void setDeliveryAddress(Address address) throws InvalidDeliveryAddressException;

    /**
     * Получить адрес доставки
     */
    Address getDeliveryAddress();

    /**
     * Рассчитать время доставки
     */
    int calculateDeliveryTime();

    /**
     * Рассчитать стоимость доставки
     */
    double calculateDeliveryCost();
}
