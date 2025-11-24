package com.pizzeria.exceptions;

/**
 * Исключение для некорректного адреса доставки
 */
public class InvalidDeliveryAddressException extends Exception {
    private String address;
    private String reason;

    public InvalidDeliveryAddressException(String address, String reason) {
        super(String.format("Некорректный адрес доставки '%s': %s", address, reason));
        this.address = address;
        this.reason = reason;
    }

    public String getAddress() {
        return address;
    }

    public String getReason() {
        return reason;
    }
}
