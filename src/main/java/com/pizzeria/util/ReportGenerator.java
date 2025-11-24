package com.pizzeria.util;

import com.pizzeria.model.Order;
import com.pizzeria.model.Pizzeria;
import com.pizzeria.model.users.Employee;

import java.time.LocalDate;
import java.util.List;

/**
 * Генератор отчетов
 */
public class ReportGenerator {

    public static String generateDailySalesReport(Pizzeria pizzeria, LocalDate date) {
        StringBuilder report = new StringBuilder();
        report.append("========== ОТЧЕТ О ПРОДАЖАХ ==========\n");
        report.append("Дата: ").append(date).append("\n");
        report.append("Пиццерия: ").append(pizzeria.getName()).append("\n");
        report.append("--------------------------------------\n");
        report.append("Общее количество заказов: ").append(pizzeria.getTotalOrders()).append("\n");
        report.append("Общая выручка: ").append(PriceCalculator.formatPrice(pizzeria.calculateDailyRevenue())).append("\n");
        report.append("======================================\n");

        return report.toString();
    }

    public static String generateEmployeeReport(Employee employee) {
        StringBuilder report = new StringBuilder();
        report.append("========== ОТЧЕТ О СОТРУДНИКЕ ==========\n");
        report.append("ФИО: ").append(employee.getFullName()).append("\n");
        report.append("ID: ").append(employee.getId()).append("\n");
        report.append("Должность: ").append(employee.getRole()).append("\n");
        report.append("Зарплата: ").append(PriceCalculator.formatPrice(employee.getSalary())).append("\n");
        report.append("Стаж: ").append(employee.getYearsOfService()).append(" лет\n");
        report.append("Статус: ").append(employee.isActive() ? "Активен" : "Неактивен").append("\n");
        report.append("========================================\n");

        return report.toString();
    }

    public static String generateInventoryReport(List<String> lowStockItems) {
        StringBuilder report = new StringBuilder();
        report.append("========== ОТЧЕТ О СКЛАДЕ ==========\n");
        report.append("Товары с низким запасом:\n");

        if (lowStockItems.isEmpty()) {
            report.append("Нет товаров с низким запасом\n");
        } else {
            for (String item : lowStockItems) {
                report.append("  - ").append(item).append("\n");
            }
        }

        report.append("====================================\n");
        return report.toString();
    }
}
