package com.pizzeria.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Генератор уникальных идентификаторов
 */
public class IdGenerator {
    private static final AtomicInteger orderCounter = new AtomicInteger(1);
    private static final AtomicInteger customerCounter = new AtomicInteger(1);
    private static final AtomicInteger employeeCounter = new AtomicInteger(1);

    public static String generateOrderId() {
        return "ORD-" + String.format("%06d", orderCounter.getAndIncrement());
    }

    public static String generateCustomerId() {
        return "CUST-" + String.format("%06d", customerCounter.getAndIncrement());
    }

    public static String generateEmployeeId() {
        return "EMP-" + String.format("%06d", employeeCounter.getAndIncrement());
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateTransactionId(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }

    public static void resetCounters() {
        orderCounter.set(1);
        customerCounter.set(1);
        employeeCounter.set(1);
    }
}
