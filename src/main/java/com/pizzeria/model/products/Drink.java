package com.pizzeria.model.products;

import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.exceptions.InvalidPriceException;

/**
 * Класс напитка
 */
public class Drink extends Product {
    private int volume; // в мл
    private boolean isCarbonated;
    private boolean isAlcoholic;
    private int temperature; // в градусах

    public Drink(String name, double basePrice, int volume) throws InvalidPriceException {
        super(name, basePrice);
        this.volume = volume;
        this.isCarbonated = false;
        this.isAlcoholic = false;
        this.temperature = 20;
    }

    @Override
    public int getPreparationTime() {
        return 2; // минуты
    }

    @Override
    public int getCalories() {
        if (name.contains("Кока-кола")) {
            return (int)(volume * 0.42); // примерно 42 калории на 100мл
        } else if (name.contains("Сок")) {
            return (int)(volume * 0.45);
        }
        return 0; // вода
    }

    @Override
    public boolean processPayment(double amount) throws InvalidPaymentException {
        if (amount < getFinalPrice()) {
            throw new InvalidPaymentException(amount, "Недостаточно средств");
        }
        return true;
    }

    public boolean isCold() {
        return temperature < 10;
    }

    public void chill() {
        this.temperature = 4;
    }

    // Getters and Setters
    public int getVolume() { return volume; }
    public void setVolume(int volume) { this.volume = volume; }

    public boolean isCarbonated() { return isCarbonated; }
    public void setCarbonated(boolean carbonated) { isCarbonated = carbonated; }

    public boolean isAlcoholic() { return isAlcoholic; }
    public void setAlcoholic(boolean alcoholic) { isAlcoholic = alcoholic; }

    public int getTemperature() { return temperature; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
}
