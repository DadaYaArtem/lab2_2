package com.pizzeria.model;

/**
 * Класс телефонного номера
 */
public class PhoneNumber {
    private String countryCode;
    private String areaCode;
    private String number;

    public PhoneNumber(String countryCode, String areaCode, String number) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.number = number;
    }

    public PhoneNumber(String fullNumber) {
        // Простая парсинг номера
        if (fullNumber.startsWith("+")) {
            this.countryCode = fullNumber.substring(0, 2);
            this.areaCode = fullNumber.substring(2, 5);
            this.number = fullNumber.substring(5);
        } else {
            this.countryCode = "+1";
            this.areaCode = "000";
            this.number = fullNumber;
        }
    }

    public String getFullNumber() {
        return countryCode + areaCode + number;
    }

    public boolean isValid() {
        return number != null && number.length() >= 7;
    }

    public String getFormattedNumber() {
        return String.format("%s (%s) %s", countryCode, areaCode, number);
    }

    // Getters and Setters
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getAreaCode() { return areaCode; }
    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    @Override
    public String toString() {
        return getFormattedNumber();
    }
}
