package com.pizzeria.model;

import com.pizzeria.model.products.Product;

/**
 * Класс элемента заказа
 */
public class OrderItem {
    private Product product;
    private int quantity;
    private String specialInstructions;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getFinalPrice() * quantity;
    }

    public int getTotalCalories() {
        return product.getCalories() * quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
        }
    }

    // Getters and Setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    @Override
    public String toString() {
        return String.format("%s x%d - %.2f руб.", product.getName(), quantity, getTotalPrice());
    }
}
