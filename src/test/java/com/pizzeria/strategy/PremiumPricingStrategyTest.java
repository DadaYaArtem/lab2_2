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
@DisplayName("Тесты для PremiumPricingStrategy")
class PremiumPricingStrategyTest {

    private PremiumPricingStrategy strategy;

    @Mock
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        strategy = new PremiumPricingStrategy(100.0);
    }

    @Test
    @DisplayName("Создание стратегии с сервисным сбором")
    void testConstructor_WithServiceFee_SetsServiceFee() {
        // Arrange & Act
        PremiumPricingStrategy newStrategy = new PremiumPricingStrategy(250.0);

        // Assert
        assertEquals(250.0, newStrategy.getServiceFee(), 0.01);
    }

    @Test
    @DisplayName("Расчет цены с сервисным сбором 100")
    void testCalculatePrice_ServiceFee100_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 1000 + 200 + 100 = 1300
        assertEquals(1300.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с нулевым сервисным сбором")
    void testCalculatePrice_ZeroServiceFee_ReturnsBasePrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(0.0);
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(600.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с большим сервисным сбором")
    void testCalculatePrice_LargeServiceFee_ReturnsCorrectPrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(500.0);
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 1000 + 200 + 500 = 1700
        assertEquals(1700.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с малым сервисным сбором")
    void testCalculatePrice_SmallServiceFee_ReturnsCorrectPrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(10.0);
        when(mockOrder.getPrice()).thenReturn(100.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(50.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 100 + 50 + 10 = 160
        assertEquals(160.0, result, 0.01);
    }

    @Test
    @DisplayName("Получение сервисного сбора")
    void testGetServiceFee_ReturnsCorrectValue() {
        // Arrange
        strategy = new PremiumPricingStrategy(150.0);

        // Act
        double serviceFee = strategy.getServiceFee();

        // Assert
        assertEquals(150.0, serviceFee, 0.01);
    }

    @Test
    @DisplayName("Установка нового сервисного сбора")
    void testSetServiceFee_UpdatesServiceFee() {
        // Arrange
        strategy = new PremiumPricingStrategy(100.0);

        // Act
        strategy.setServiceFee(300.0);

        // Assert
        assertEquals(300.0, strategy.getServiceFee(), 0.01);
    }

    @Test
    @DisplayName("Изменение сервисного сбора влияет на расчет цены")
    void testSetServiceFee_AffectsPriceCalculation() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double priceBefore = strategy.calculatePrice(mockOrder);
        strategy.setServiceFee(200.0);
        double priceAfter = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(1100.0, priceBefore, 0.01);  // 1000 + 100
        assertEquals(1200.0, priceAfter, 0.01);   // 1000 + 200
    }

    @Test
    @DisplayName("Расчет цены без доставки")
    void testCalculatePrice_NoDelivery_AddsServiceFeeToBasePrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(150.0);
        when(mockOrder.getPrice()).thenReturn(800.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(950.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены только с доставкой и сервисным сбором")
    void testCalculatePrice_OnlyDeliveryAndServiceFee_ReturnsCorrectPrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(50.0);
        when(mockOrder.getPrice()).thenReturn(0.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(150.0, result, 0.01);
    }

    @Test
    @DisplayName("Стратегия реализует интерфейс PriceCalculationStrategy")
    void testStrategy_ImplementsInterface() {
        // Assert
        assertTrue(strategy instanceof PriceCalculationStrategy);
    }

    @Test
    @DisplayName("Расчет цены несколько раз с одним заказом")
    void testCalculatePrice_MultipleCalls_ReturnsConsistentResults() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result1 = strategy.calculatePrice(mockOrder);
        double result2 = strategy.calculatePrice(mockOrder);
        double result3 = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(700.0, result1, 0.01);
        assertEquals(700.0, result2, 0.01);
        assertEquals(700.0, result3, 0.01);
    }

    @Test
    @DisplayName("Сервисный сбор добавляется к сумме базовой цены и доставки")
    void testCalculatePrice_ServiceFeeAddedToTotal() {
        // Arrange
        strategy = new PremiumPricingStrategy(75.0);
        when(mockOrder.getPrice()).thenReturn(425.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 425 + 100 + 75 = 600
        assertEquals(600.0, result, 0.01);
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Расчет цены с дробными значениями")
    void testCalculatePrice_DecimalValues_ReturnsCorrectPrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(99.99);
        when(mockOrder.getPrice()).thenReturn(123.45);
        when(mockOrder.calculateDeliveryCost()).thenReturn(67.89);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 123.45 + 67.89 + 99.99 = 291.33
        assertEquals(291.33, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с большой базовой ценой")
    void testCalculatePrice_LargeBasePrice_ReturnsCorrectPrice() {
        // Arrange
        strategy = new PremiumPricingStrategy(200.0);
        when(mockOrder.getPrice()).thenReturn(10000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(500.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // 10000 + 500 + 200 = 10700
        assertEquals(10700.0, result, 0.01);
    }

    @Test
    @DisplayName("Различные стратегии с разными сервисными сборами")
    void testMultipleStrategies_DifferentServiceFees_ReturnDifferentPrices() {
        // Arrange
        PremiumPricingStrategy strategy50 = new PremiumPricingStrategy(50.0);
        PremiumPricingStrategy strategy100 = new PremiumPricingStrategy(100.0);
        PremiumPricingStrategy strategy200 = new PremiumPricingStrategy(200.0);

        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result50 = strategy50.calculatePrice(mockOrder);
        double result100 = strategy100.calculatePrice(mockOrder);
        double result200 = strategy200.calculatePrice(mockOrder);

        // Assert
        assertEquals(1050.0, result50, 0.01);
        assertEquals(1100.0, result100, 0.01);
        assertEquals(1200.0, result200, 0.01);
    }

    @Test
    @DisplayName("Сервисный сбор увеличивает итоговую цену")
    void testCalculatePrice_ServiceFeeIncreasesTotal() {
        // Arrange
        strategy = new PremiumPricingStrategy(300.0);
        when(mockOrder.getPrice()).thenReturn(700.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // Итоговая цена должна быть больше базовой цены + доставка
        assertTrue(result > 800.0);
        assertEquals(1100.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены вызывает методы заказа один раз")
    void testCalculatePrice_CallsOrderMethodsOnce() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        strategy.calculatePrice(mockOrder);

        // Assert
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Стратегия не изменяет заказ")
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
    @DisplayName("Премиум стратегия всегда добавляет фиксированную сумму")
    void testCalculatePrice_AlwaysAddsFixedAmount() {
        // Arrange
        double serviceFee = 125.0;
        strategy = new PremiumPricingStrategy(serviceFee);

        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        when(order1.getPrice()).thenReturn(100.0);
        when(order1.calculateDeliveryCost()).thenReturn(50.0);

        when(order2.getPrice()).thenReturn(500.0);
        when(order2.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result1 = strategy.calculatePrice(order1);
        double result2 = strategy.calculatePrice(order2);

        // Assert
        // Разница между результатами должна равняться разнице базовых сумм
        assertEquals(275.0, result1, 0.01);  // 100 + 50 + 125
        assertEquals(825.0, result2, 0.01);  // 500 + 200 + 125
        assertEquals(550.0, result2 - result1, 0.01);  // (500+200) - (100+50)
    }

    @Test
    @DisplayName("Сравнение премиум стратегии со стандартной")
    void testCalculatePrice_CompareWithStandard_PremiumIsHigher() {
        // Arrange
        StandardPricingStrategy standardStrategy = new StandardPricingStrategy();
        PremiumPricingStrategy premiumStrategy = new PremiumPricingStrategy(150.0);

        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double standardPrice = standardStrategy.calculatePrice(mockOrder);
        double premiumPrice = premiumStrategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(1200.0, standardPrice, 0.01);
        assertEquals(1350.0, premiumPrice, 0.01);
        assertTrue(premiumPrice > standardPrice);
        assertEquals(150.0, premiumPrice - standardPrice, 0.01);
    }
}
