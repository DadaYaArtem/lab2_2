package com.pizzeria.model.users;

import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.model.products.Pizza;

/**
 * Класс повара
 */
public class Chef extends Employee {
    private String specialty;
    private int pizzasCooked;
    private int experienceYears;

    public Chef(String id, String firstName, String lastName, double salary) {
        super(id, firstName, lastName, EmployeeRole.CHEF, salary);
        this.pizzasCooked = 0;
        this.experienceYears = 0;
    }

    @Override
    public String getRole() {
        return "Повар";
    }

    @Override
    public void performDuty() {
        System.out.println(getFullName() + " готовит пиццу");
    }

    public int cookPizza(Pizza pizza) throws InsufficientIngredientsException {
        int cookingTime = pizza.cook();
        pizzasCooked++;
        return cookingTime;
    }

    public boolean canCookPizza(String pizzaType) {
        if (specialty != null && specialty.equalsIgnoreCase(pizzaType)) {
            return true;
        }
        return experienceYears > 2; // опытный повар может готовить любую пиццу
    }

    public String getSkillLevel() {
        if (experienceYears >= 10) return "Мастер";
        if (experienceYears >= 5) return "Профессионал";
        if (experienceYears >= 2) return "Опытный";
        return "Новичок";
    }

    // Getters and Setters
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public int getPizzasCooked() { return pizzasCooked; }
    public void incrementPizzasCooked() { this.pizzasCooked++; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
}
