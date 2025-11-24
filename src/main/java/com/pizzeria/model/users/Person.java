package com.pizzeria.model.users;

import com.pizzeria.model.Address;
import com.pizzeria.model.Email;
import com.pizzeria.model.PhoneNumber;

/**
 * Абстрактный класс человека
 */
public abstract class Person {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected PhoneNumber phoneNumber;
    protected Email email;
    protected Address address;

    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public abstract String getRole();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(PhoneNumber phoneNumber) { this.phoneNumber = phoneNumber; }

    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("%s (ID: %s)", getFullName(), id);
    }
}
