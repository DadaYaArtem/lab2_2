package com.pizzeria.model;

/**
 * Класс электронной почты
 */
public class Email {
    private String localPart;
    private String domain;

    public Email(String email) {
        if (email != null && email.contains("@")) {
            String[] parts = email.split("@");
            this.localPart = parts[0];
            this.domain = parts[1];
        }
    }

    public Email(String localPart, String domain) {
        this.localPart = localPart;
        this.domain = domain;
    }

    public String getFullEmail() {
        return localPart + "@" + domain;
    }

    public boolean isValid() {
        return localPart != null && !localPart.isEmpty() &&
               domain != null && domain.contains(".");
    }

    public String maskEmail() {
        if (localPart.length() <= 2) {
            return "***@" + domain;
        }
        return localPart.substring(0, 2) + "***@" + domain;
    }

    // Getters and Setters
    public String getLocalPart() { return localPart; }
    public void setLocalPart(String localPart) { this.localPart = localPart; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    @Override
    public String toString() {
        return getFullEmail();
    }
}
