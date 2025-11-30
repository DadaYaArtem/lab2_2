package com.pizzeria.util;

import com.pizzeria.model.Pizzeria;
import com.pizzeria.model.users.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для ReportGenerator")
class ReportGeneratorTest {

    @Mock
    private Pizzeria mockPizzeria;

    @Mock
    private Employee mockEmployee;

    @Test
    @DisplayName("Генерация отчета о продажах за день")
    void testGenerateDailySalesReport_ValidData_ReturnsReport() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 15);
        when(mockPizzeria.getName()).thenReturn("Пиццерия У Марио");
        when(mockPizzeria.getTotalOrders()).thenReturn(25);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(15000.0);

        // Act
        String report = ReportGenerator.generateDailySalesReport(mockPizzeria, date);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("ОТЧЕТ О ПРОДАЖАХ"));
        assertTrue(report.contains("2024-01-15"));
        assertTrue(report.contains("Пиццерия У Марио"));
        assertTrue(report.contains("25"));
        assertTrue(report.contains("15000"));
    }

    @Test
    @DisplayName("Отчет о продажах содержит правильную структуру")
    void testGenerateDailySalesReport_HasCorrectStructure() {
        // Arrange
        LocalDate date = LocalDate.now();
        when(mockPizzeria.getName()).thenReturn("Тестовая пиццерия");
        when(mockPizzeria.getTotalOrders()).thenReturn(10);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(5000.0);

        // Act
        String report = ReportGenerator.generateDailySalesReport(mockPizzeria, date);

        // Assert
        assertTrue(report.contains("=========="));
        assertTrue(report.contains("Дата:"));
        assertTrue(report.contains("Пиццерия:"));
        assertTrue(report.contains("Общее количество заказов:"));
        assertTrue(report.contains("Общая выручка:"));
        assertTrue(report.contains("руб."));
    }

    @Test
    @DisplayName("Отчет о продажах с нулевыми заказами")
    void testGenerateDailySalesReport_ZeroOrders_ReturnsReport() {
        // Arrange
        LocalDate date = LocalDate.now();
        when(mockPizzeria.getName()).thenReturn("Пустая пиццерия");
        when(mockPizzeria.getTotalOrders()).thenReturn(0);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(0.0);

        // Act
        String report = ReportGenerator.generateDailySalesReport(mockPizzeria, date);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("0"));
        assertTrue(report.contains("0,00 руб."));
    }

    @Test
    @DisplayName("Отчет о продажах с большой выручкой")
    void testGenerateDailySalesReport_LargeRevenue_ReturnsReport() {
        // Arrange
        LocalDate date = LocalDate.now();
        when(mockPizzeria.getName()).thenReturn("Успешная пиццерия");
        when(mockPizzeria.getTotalOrders()).thenReturn(500);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(250000.0);

        // Act
        String report = ReportGenerator.generateDailySalesReport(mockPizzeria, date);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("500"));
        assertTrue(report.contains("250000"));
    }

    @Test
    @DisplayName("Генерация отчета о сотруднике")
    void testGenerateEmployeeReport_ValidEmployee_ReturnsReport() {
        // Arrange
        when(mockEmployee.getFullName()).thenReturn("Иван Иванов");
        when(mockEmployee.getId()).thenReturn("EMP-001");
        when(mockEmployee.getRole()).thenReturn("Повар");
        when(mockEmployee.getSalary()).thenReturn(50000.0);
        when(mockEmployee.getYearsOfService()).thenReturn(5);
        when(mockEmployee.isActive()).thenReturn(true);

        // Act
        String report = ReportGenerator.generateEmployeeReport(mockEmployee);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("ОТЧЕТ О СОТРУДНИКЕ"));
        assertTrue(report.contains("Иван Иванов"));
        assertTrue(report.contains("EMP-001"));
        assertTrue(report.contains("Повар"));
        assertTrue(report.contains("50000"));
        assertTrue(report.contains("5 лет"));
        assertTrue(report.contains("Активен"));
    }

    @Test
    @DisplayName("Отчет о неактивном сотруднике")
    void testGenerateEmployeeReport_InactiveEmployee_ShowsInactive() {
        // Arrange
        when(mockEmployee.getFullName()).thenReturn("Петр Петров");
        when(mockEmployee.getId()).thenReturn("EMP-002");
        when(mockEmployee.getRole()).thenReturn("Официант");
        when(mockEmployee.getSalary()).thenReturn(30000.0);
        when(mockEmployee.getYearsOfService()).thenReturn(2);
        when(mockEmployee.isActive()).thenReturn(false);

        // Act
        String report = ReportGenerator.generateEmployeeReport(mockEmployee);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("Неактивен"));
        assertFalse(report.contains("Активен"));
    }

    @Test
    @DisplayName("Отчет о сотруднике содержит правильную структуру")
    void testGenerateEmployeeReport_HasCorrectStructure() {
        // Arrange
        when(mockEmployee.getFullName()).thenReturn("Тест Тестов");
        when(mockEmployee.getId()).thenReturn("EMP-999");
        when(mockEmployee.getRole()).thenReturn("Менеджер");
        when(mockEmployee.getSalary()).thenReturn(70000.0);
        when(mockEmployee.getYearsOfService()).thenReturn(10);
        when(mockEmployee.isActive()).thenReturn(true);

        // Act
        String report = ReportGenerator.generateEmployeeReport(mockEmployee);

        // Assert
        assertTrue(report.contains("=========="));
        assertTrue(report.contains("ФИО:"));
        assertTrue(report.contains("ID:"));
        assertTrue(report.contains("Должность:"));
        assertTrue(report.contains("Зарплата:"));
        assertTrue(report.contains("Стаж:"));
        assertTrue(report.contains("Статус:"));
        assertTrue(report.contains("руб."));
    }

    @Test
    @DisplayName("Отчет о сотруднике с нулевым стажем")
    void testGenerateEmployeeReport_ZeroYearsOfService_ReturnsReport() {
        // Arrange
        when(mockEmployee.getFullName()).thenReturn("Новичок Новичков");
        when(mockEmployee.getId()).thenReturn("EMP-100");
        when(mockEmployee.getRole()).thenReturn("Стажер");
        when(mockEmployee.getSalary()).thenReturn(25000.0);
        when(mockEmployee.getYearsOfService()).thenReturn(0);
        when(mockEmployee.isActive()).thenReturn(true);

        // Act
        String report = ReportGenerator.generateEmployeeReport(mockEmployee);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("0 лет"));
    }

    @Test
    @DisplayName("Генерация отчета о складе с товарами")
    void testGenerateInventoryReport_WithItems_ReturnsReport() {
        // Arrange
        List<String> lowStockItems = Arrays.asList(
            "Моцарелла - 5 кг",
            "Помидоры - 2 кг",
            "Пепперони - 1 кг"
        );

        // Act
        String report = ReportGenerator.generateInventoryReport(lowStockItems);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("ОТЧЕТ О СКЛАДЕ"));
        assertTrue(report.contains("Товары с низким запасом"));
        assertTrue(report.contains("Моцарелла - 5 кг"));
        assertTrue(report.contains("Помидоры - 2 кг"));
        assertTrue(report.contains("Пепперони - 1 кг"));
    }

    @Test
    @DisplayName("Генерация отчета о складе без товаров")
    void testGenerateInventoryReport_EmptyList_ReturnsReport() {
        // Arrange
        List<String> lowStockItems = new ArrayList<>();

        // Act
        String report = ReportGenerator.generateInventoryReport(lowStockItems);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("ОТЧЕТ О СКЛАДЕ"));
        assertTrue(report.contains("Нет товаров с низким запасом"));
    }

    @Test
    @DisplayName("Отчет о складе содержит правильную структуру")
    void testGenerateInventoryReport_HasCorrectStructure() {
        // Arrange
        List<String> lowStockItems = Arrays.asList("Товар 1");

        // Act
        String report = ReportGenerator.generateInventoryReport(lowStockItems);

        // Assert
        assertTrue(report.contains("=========="));
        assertTrue(report.contains("ОТЧЕТ О СКЛАДЕ"));
        assertTrue(report.contains("===================================="));
    }

    @Test
    @DisplayName("Отчет о складе с одним товаром")
    void testGenerateInventoryReport_SingleItem_ReturnsReport() {
        // Arrange
        List<String> lowStockItems = Arrays.asList("Сыр Пармезан - 3 кг");

        // Act
        String report = ReportGenerator.generateInventoryReport(lowStockItems);

        // Assert
        assertNotNull(report);
        assertTrue(report.contains("Сыр Пармезан - 3 кг"));
        assertTrue(report.contains("  - "));
    }

    @Test
    @DisplayName("Отчет о складе с большим количеством товаров")
    void testGenerateInventoryReport_ManyItems_ReturnsReport() {
        // Arrange
        List<String> lowStockItems = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            lowStockItems.add("Товар " + i);
        }

        // Act
        String report = ReportGenerator.generateInventoryReport(lowStockItems);

        // Assert
        assertNotNull(report);
        for (int i = 1; i <= 10; i++) {
            assertTrue(report.contains("Товар " + i));
        }
    }

    @Test
    @DisplayName("Все отчеты являются непустыми строками")
    void testAllReports_NotEmpty() {
        // Arrange
        LocalDate date = LocalDate.now();
        when(mockPizzeria.getName()).thenReturn("Пиццерия");
        when(mockPizzeria.getTotalOrders()).thenReturn(1);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(100.0);

        when(mockEmployee.getFullName()).thenReturn("Сотрудник");
        when(mockEmployee.getId()).thenReturn("E1");
        when(mockEmployee.getRole()).thenReturn("Роль");
        when(mockEmployee.getSalary()).thenReturn(1000.0);
        when(mockEmployee.getYearsOfService()).thenReturn(1);
        when(mockEmployee.isActive()).thenReturn(true);

        List<String> items = Arrays.asList("Товар");

        // Act
        String salesReport = ReportGenerator.generateDailySalesReport(mockPizzeria, date);
        String employeeReport = ReportGenerator.generateEmployeeReport(mockEmployee);
        String inventoryReport = ReportGenerator.generateInventoryReport(items);

        // Assert
        assertFalse(salesReport.isEmpty());
        assertFalse(employeeReport.isEmpty());
        assertFalse(inventoryReport.isEmpty());
    }

    @Test
    @DisplayName("Отчет о продажах содержит дату в правильном формате")
    void testGenerateDailySalesReport_DateFormat_Correct() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 25);
        when(mockPizzeria.getName()).thenReturn("Пиццерия");
        when(mockPizzeria.getTotalOrders()).thenReturn(1);
        when(mockPizzeria.calculateDailyRevenue()).thenReturn(100.0);

        // Act
        String report = ReportGenerator.generateDailySalesReport(mockPizzeria, date);

        // Assert
        assertTrue(report.contains("2024-12-25"));
    }
}
