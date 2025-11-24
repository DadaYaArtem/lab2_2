package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDeliveryAddressException;

/**
 * Класс адреса
 */
public class Address {
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String city;
    private String postalCode;
    private double latitude;
    private double longitude;

    public Address(String street, String houseNumber, String city, String postalCode)
            throws InvalidDeliveryAddressException {
        validateAddress(street, houseNumber, city, postalCode);
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
    }

    private void validateAddress(String street, String houseNumber, String city, String postalCode)
            throws InvalidDeliveryAddressException {
        if (street == null || street.trim().isEmpty()) {
            throw new InvalidDeliveryAddressException(street, "Улица не может быть пустой");
        }
        if (houseNumber == null || houseNumber.trim().isEmpty()) {
            throw new InvalidDeliveryAddressException(houseNumber, "Номер дома не может быть пустым");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new InvalidDeliveryAddressException(city, "Город не может быть пустым");
        }
    }

    public double calculateDistance(Address other) {
        // Упрощенный расчет расстояния
        double dx = this.latitude - other.latitude;
        double dy = this.longitude - other.longitude;
        return Math.sqrt(dx * dx + dy * dy) * 111; // примерно в км
    }

    // Getters and Setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return String.format("%s, д. %s%s, %s, %s",
                street, houseNumber,
                apartmentNumber != null ? ", кв. " + apartmentNumber : "",
                city, postalCode);
    }
}
