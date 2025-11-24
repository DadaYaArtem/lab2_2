package com.pizzeria.service;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.exceptions.DuplicateOrderException;
import com.pizzeria.exceptions.OrderNotFoundException;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис управления заказами
 */
public class OrderService {
    private Map<String, Order> orders;
    private int orderCounter;

    public OrderService() {
        this.orders = new HashMap<>();
        this.orderCounter = 1;
    }

    public Order createOrder(Customer customer) throws DuplicateOrderException {
        String orderId = "ORD-" + (orderCounter++);
        if (orders.containsKey(orderId)) {
            throw new DuplicateOrderException(orderId);
        }

        Order order = new Order(orderId, customer);
        orders.put(orderId, order);
        customer.addToOrderHistory(orderId);

        System.out.println("Создан заказ #" + orderId + " для клиента " + customer.getFullName());
        return order;
    }

    public Order getOrder(String orderId) throws OrderNotFoundException {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    public void cancelOrder(String orderId) throws OrderNotFoundException {
        Order order = getOrder(orderId);
        order.updateStatus(OrderStatus.CANCELLED);
        System.out.println("Заказ #" + orderId + " отменен");
    }

    public void updateOrderStatus(String orderId, OrderStatus status) throws OrderNotFoundException {
        Order order = getOrder(orderId);
        order.updateStatus(status);
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (Order order : orders.values()) {
            if (order.isPaid()) {
                total += order.getFinalPrice();
            }
        }
        return total;
    }

    public int getOrderCount() {
        return orders.size();
    }

    public Map<String, Order> getAllOrders() {
        return new HashMap<>(orders);
    }
}
