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
@DisplayName("Тесты для DiscountPricingStrategy")
class DiscountPricingStrategyTest {

    private DiscountPricingStrategy strategy;

    @Mock
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        strategy = new DiscountPricingStrategy(10.0);
    }

    @Test
    @DisplayName("Создание стратегии с процентом скидки")
    void testConstructor_WithDiscount_SetsDiscountPercentage() {
        // Arrange & Act
        DiscountPricingStrategy newStrategy = new DiscountPricingStrategy(20.0);

        // Assert
        assertEquals(20.0, newStrategy.getDiscountPercentage(), 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой 10%")
    void testCalculatePrice_TenPercentDiscount_ReturnsCorrectPrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // (1000 + 200) * 0.9 = 1080
        assertEquals(1080.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой 50%")
    void testCalculatePrice_FiftyPercentDiscount_ReturnsHalfPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(50.0);
        when(mockOrder.getPrice()).thenReturn(800.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(200.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // (800 + 200) * 0.5 = 500
        assertEquals(500.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой 100%")
    void testCalculatePrice_HundredPercentDiscount_ReturnsZero() {
        // Arrange
        strategy = new DiscountPricingStrategy(100.0);
        when(mockOrder.getPrice()).thenReturn(1500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(300.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(0.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с нулевой скидкой")
    void testCalculatePrice_ZeroDiscount_ReturnsFullPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(0.0);
        when(mockOrder.getPrice()).thenReturn(600.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(700.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой 25%")
    void testCalculatePrice_TwentyFivePercentDiscount_ReturnsCorrectPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(25.0);
        when(mockOrder.getPrice()).thenReturn(400.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // (400 + 100) * 0.75 = 375
        assertEquals(375.0, result, 0.01);
    }

    @Test
    @DisplayName("Получение процента скидки")
    void testGetDiscountPercentage_ReturnsCorrectValue() {
        // Arrange
        strategy = new DiscountPricingStrategy(15.0);

        // Act
        double discount = strategy.getDiscountPercentage();

        // Assert
        assertEquals(15.0, discount, 0.01);
    }

    @Test
    @DisplayName("Установка нового процента скидки")
    void testSetDiscountPercentage_UpdatesDiscount() {
        // Arrange
        strategy = new DiscountPricingStrategy(10.0);

        // Act
        strategy.setDiscountPercentage(30.0);

        // Assert
        assertEquals(30.0, strategy.getDiscountPercentage(), 0.01);
    }

    @Test
    @DisplayName("Изменение скидки влияет на расчет цены")
    void testSetDiscountPercentage_AffectsPriceCalculation() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double priceBefore = strategy.calculatePrice(mockOrder);
        strategy.setDiscountPercentage(20.0);
        double priceAfter = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(900.0, priceBefore, 0.01);  // 10% скидка
        assertEquals(800.0, priceAfter, 0.01);   // 20% скидка
    }

    @Test
    @DisplayName("Расчет цены без доставки")
    void testCalculatePrice_NoDelivery_AppliesDiscountToBasePrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(20.0);
        when(mockOrder.getPrice()).thenReturn(500.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(400.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с малой суммой")
    void testCalculatePrice_SmallAmount_ReturnsCorrectPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(10.0);
        when(mockOrder.getPrice()).thenReturn(10.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(5.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(13.5, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с большой суммой")
    void testCalculatePrice_LargeAmount_ReturnsCorrectPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(15.0);
        when(mockOrder.getPrice()).thenReturn(10000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(500.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // (10000 + 500) * 0.85 = 8925
        assertEquals(8925.0, result, 0.01);
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
        when(mockOrder.getPrice()).thenReturn(600.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result1 = strategy.calculatePrice(mockOrder);
        double result2 = strategy.calculatePrice(mockOrder);
        double result3 = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(630.0, result1, 0.01);
        assertEquals(630.0, result2, 0.01);
        assertEquals(630.0, result3, 0.01);
    }

    @Test
    @DisplayName("Скидка применяется к сумме базовой цены и доставки")
    void testCalculatePrice_DiscountAppliedToTotal() {
        // Arrange
        strategy = new DiscountPricingStrategy(10.0);
        when(mockOrder.getPrice()).thenReturn(900.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(100.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // Сначала суммируется: 900 + 100 = 1000
        // Затем применяется скидка: 1000 * 0.9 = 900
        assertEquals(900.0, result, 0.01);
        verify(mockOrder, times(1)).getPrice();
        verify(mockOrder, times(1)).calculateDeliveryCost();
    }

    @Test
    @DisplayName("Расчет цены с дробными значениями")
    void testCalculatePrice_DecimalValues_ReturnsCorrectPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(12.5);
        when(mockOrder.getPrice()).thenReturn(123.45);
        when(mockOrder.calculateDeliveryCost()).thenReturn(67.89);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        // (123.45 + 67.89) * 0.875 = 167.4225
        assertEquals(167.42, result, 0.01);
    }

    @Test
    @DisplayName("Скидка 1% дает почти полную цену")
    void testCalculatePrice_OnePercentDiscount_ReturnsAlmostFullPrice() {
        // Arrange
        strategy = new DiscountPricingStrategy(1.0);
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(990.0, result, 0.01);
    }

    @Test
    @DisplayName("Скидка 99% дает почти нулевую цену")
    void testCalculatePrice_NinetyNinePercentDiscount_ReturnsAlmostZero() {
        // Arrange
        strategy = new DiscountPricingStrategy(99.0);
        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result = strategy.calculatePrice(mockOrder);

        // Assert
        assertEquals(10.0, result, 0.01);
    }

    @Test
    @DisplayName("Различные стратегии с разными скидками")
    void testMultipleStrategies_DifferentDiscounts_ReturnDifferentPrices() {
        // Arrange
        DiscountPricingStrategy strategy10 = new DiscountPricingStrategy(10.0);
        DiscountPricingStrategy strategy20 = new DiscountPricingStrategy(20.0);
        DiscountPricingStrategy strategy30 = new DiscountPricingStrategy(30.0);

        when(mockOrder.getPrice()).thenReturn(1000.0);
        when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);

        // Act
        double result10 = strategy10.calculatePrice(mockOrder);
        double result20 = strategy20.calculatePrice(mockOrder);
        double result30 = strategy30.calculatePrice(mockOrder);

        // Assert
        assertEquals(900.0, result10, 0.01);
        assertEquals(800.0, result20, 0.01);
        assertEquals(700.0, result30, 0.01);
    }
}
