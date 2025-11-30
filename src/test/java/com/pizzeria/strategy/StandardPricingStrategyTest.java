package com.pizzeria.strategy;

import com.pizzeria.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для StandardPricingStrategy")
class StandardPricingStrategyTest {

    private StandardPricingStrategy strategy;

    @Mock
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        strategy = new StandardPricingStrategy();
    }

    @Test
    @DisplayName("Расчет цены с базовой ценой и стоимостью доставки")
    void testCalculatePrice_WithDelivery_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(1200.0, result, 0.01);
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Расчет цены без стоимости доставки")
    void testCalculatePrice_NoDelivery_ReturnsBasePrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(500.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены для нулевой базовой цены")
    void testCalculatePrice_ZeroBasePrice_ReturnsDeliveryCost() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(0.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(150.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(150.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены для больших сумм")
    void testCalculatePrice_LargeAmounts_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(10000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(500.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(10500.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены для малых сумм")
    void testCalculatePrice_SmallAmounts_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(100.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(50.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(150.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены несколько раз с одинаковым заказом")
    void testCalculatePrice_MultipleCalls_ReturnsConsistentResults() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(750.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(250.0);

        // Act
        double result1 = strategy.calculatePrice(mockOrder);
        double result2 = strategy.calculatePrice(mockOrder);
        double result3 = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(1000.0, result1, 0.01);
        assertEquals(1000.0, result2, 0.01);
        assertEquals(1000.0, result3, 0.01);
    }

    @Test
    @DisplayName("Расчет цены вызывает методы заказа один раз")
    void testCalculatePrice_CallsOrderMethodsOnce() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(300.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        strategy.calculatePrice(mockOrder);

        // Assert
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Стратегия реализует интерфейс PriceCalculationStrategy")
    void testStrategy_ImplementsInterface() {
        // Assert
        assertTrue(strategy instanceof PriceCalculationStrategy);
    }

    @Test
    @DisplayName("Расчет цены с дробными значениями")
    void testCalculatePrice_DecimalValues_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(123.45);
        when(mockOrder.calculateDeliveryCost()).thenReturn(67.89);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(191.34, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с различными заказами")
    void testCalculatePrice_DifferentOrders_ReturnsDifferentPrices() {
        // Arrange
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        when(order1.getPrice()).thenReturn(500.0);
        when(order1.calculateDeliveryCost()).thenReturn(100.0);

        when(order2.getPrice()).thenReturn(800.0);
        when(order2.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result1 = strategy.calculatePrice(order1);
        double result2 = strategy.calculatePrice(order2);

        // Assert
        assertEquals(600.0, result1, 0.01);
        assertEquals(1000.0, result2, 0.01);
        assertNotEquals(result1, result2);
    }

    @Test
    @DisplayName("Расчет цены корректно суммирует компоненты")
    void testCalculatePrice_SumsComponentsCorrectly() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(999.99);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.01);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(1000.0, result, 0.01);
    }

    @Test
    @DisplayName("Стандартная стратегия не изменяет базовую цену заказа")
    void testCalculatePrice_DoesNotModifyOrder() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        strategy.calculatePrice(mockOrder);

        // Assert
        verify(mockOrder, never()).setPrice(anyDouble());
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Расчет цены для заказа с высокой стоимостью доставки")
    void testCalculatePrice_HighDeliveryCost_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(100.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(500.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(600.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены всегда неотрицательный")
    void testCalculatePrice_AlwaysNonNegative() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(0.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertTrue(result >= 0.0);
    }
}
