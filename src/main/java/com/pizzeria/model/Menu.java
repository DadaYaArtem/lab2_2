package com.pizzeria.model;

import com.pizzeria.model.products.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс меню
 */
public class Menu {
    private List<Product> products;
    private String name;
    private boolean isActive;

    public Menu(String name) {
        this.name = name;
        this.products = new ArrayList<>();
        this.isActive = true;
    }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Добавлен продукт в меню: " + product.getName());
    }

    public void removeProduct(Product product) {
        products.remove(product);
        System.out.println("Удален продукт из меню: " + product.getName());
    }

    public List<Product> getAvailableProducts() {
        return products.stream()
            .filter(Product::isAvailable)
            .collect(Collectors.toList());
    }

    public Product findProductByName(String name) {
        return products.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }

    public void displayMenu() {
        System.out.println("========== " + name + " ==========");
        for (Product product : products) {
            if (product.isAvailable()) {
                System.out.println(product);
            }
        }
        System.out.println("=====================================");
    }

    // Getters and Setters
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
